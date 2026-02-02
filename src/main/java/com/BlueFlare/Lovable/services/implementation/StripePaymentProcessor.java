package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.subscription.CheckoutRequest;
import com.BlueFlare.Lovable.dto.subscription.CheckoutResponse;
import com.BlueFlare.Lovable.dto.subscription.PortalReponse;
import com.BlueFlare.Lovable.entity.Plan;
import com.BlueFlare.Lovable.entity.User;
import com.BlueFlare.Lovable.enums.SubscriptionStatus;
import com.BlueFlare.Lovable.error.ResourceNotFoundException;
import com.BlueFlare.Lovable.repository.PlanRepository;
import com.BlueFlare.Lovable.repository.UserRepository;
import com.BlueFlare.Lovable.security.AuthUtil;
import com.BlueFlare.Lovable.services.PaymentProcessor;
import com.BlueFlare.Lovable.services.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripePaymentProcessor implements PaymentProcessor {

    @Value("${client.url}")
    private String frontendUrl;
    private final AuthUtil authUtil;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

    @Override
    public CheckoutResponse createCheckOutSessionUrl(CheckoutRequest checkoutRequest) {

        Plan plan = planRepository.findById(checkoutRequest.planId()).orElseThrow(
                ()-> new ResourceNotFoundException("Plan", checkoutRequest.planId().toString()));


        Long userId = authUtil.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("user", userId.toString()));


        var params = SessionCreateParams.builder().
                addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(plan.getStripePriceId()).setQuantity(1L).build()
                )
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSubscriptionData(
                        new SessionCreateParams.SubscriptionData.Builder()
                                .setBillingMode(SessionCreateParams.SubscriptionData.BillingMode.builder()
                                .setType(SessionCreateParams.SubscriptionData.BillingMode.Type.FLEXIBLE)
                                .build())
                                .build()
                )
                .setSuccessUrl(frontendUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendUrl + "/cancel.html")
                .putMetadata("user_id", userId.toString())
                .putMetadata("plan_id",plan.getId().toString());

        try {
            String stripeCustomerId = user.getStripeCustomerId();
            if(stripeCustomerId==null || stripeCustomerId.isEmpty()){
                params.setCustomerEmail(user.getUsername());
            } else {
                params.setCustomer(stripeCustomerId);
            }
            Session session = Session.create(params.build());
            return new CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public PortalReponse openCustomerPortal() {
        return null;
    }

    @Override
    public void handleWebHook(String type, StripeObject stripeObject, Map<String, String> metadata) {

        log.debug("Handling stripe event: {}", type);

        switch(type) {
            case "checkout.session.completed" -> handleCheckoutSessionCompleted((Session) stripeObject, metadata); //one time event on checkout completed
            case "customer.subscription.updated" -> handleCustomerSubscriptionUpdated((Subscription) stripeObject); //when user cancels, upgrades or any other updates
            case "customer.subscription.deleted" -> handleCustomerSubscriptionDeleted((Subscription) stripeObject); //when subscription ends
            case "invoice.paid" -> handleInvoicePaid((Invoice) stripeObject); //when invoice is paid
            case "invoice.payment_failed" -> handleInvoicePaymentFailed((Invoice) stripeObject); //when invoice is not paid, mark as past done
            default -> log.debug("Ignoring the event: {}", type);
        }
        
    }

    private void handleCheckoutSessionCompleted(Session session, Map<String, String> metadata){

        if(session==null){
            log.error("session object was null");
            return;
        }
        Long userId = Long.parseLong(metadata.get("user_id"));
        Long planId = Long.parseLong(metadata.get("plan_id"));

        String subscriptionId = session.getSubscription();
        String customerId = session.getCustomer();

        User user = getUser(userId);

        if(user.getStripeCustomerId()==null){
            user.setStripeCustomerId(customerId);
            userRepository.save(user);
        }

        subscriptionService.activateSubscription(userId, planId, subscriptionId, customerId);
    }

    private void handleCustomerSubscriptionUpdated(Subscription subscription){

        if(subscription==null){
            log.error("Subscription object was null");
            return;
        }

        SubscriptionStatus status = mapStripeStatusToEnums(subscription.getStatus());
        if(status==null){
            log.warn("Unknown status '{}' for subscription {} ", subscription.getStatus(), subscription.getId());
            return;
        }

        SubscriptionItem item = subscription.getItems().getData().get(0);
        Instant periodStart = toInstant(item.getCurrentPeriodStart());
        Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

        Long planId = resolvePlanId(item.getPrice());

        subscriptionService.updateSubscription(
                subscription.getId(), status, periodStart, periodEnd,
                subscription.getCancelAtPeriodEnd(), planId
        );
    }


    private void handleCustomerSubscriptionDeleted(Subscription subscription){

        if(subscription==null){
            log.error("Subscription object was null");
            return;
        }

        subscriptionService.cancelSubscription(subscription.getId());

    }
    private void handleInvoicePaid(Invoice invoice){
        String subId = extractSubscriptionId(invoice);

        if(subId==null) return;
        try {
            Subscription subscription = Subscription.retrieve(subId);

            var item = subscription.getItems().getData().get(0);
            Instant periodStart = toInstant(item.getCurrentPeriodStart());
            Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

            subscriptionService.renewSubscriptionPeriod(
                    subId,
                    periodStart,
                    periodEnd
            );
        } catch (StripeException e){
            throw new RuntimeException(e);
        }
    }
    private void handleInvoicePaymentFailed(Invoice invoice){

        String subId = extractSubscriptionId(invoice);
        if(subId == null) return;

        subscriptionService.markSubscriptionPastDue(subId);

    }

    private User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("user", userId.toString()));
    }

    private SubscriptionStatus mapStripeStatusToEnums(String status) {
        return switch (status) {
            case "active" ->SubscriptionStatus.ACTIVE;
            case "trialing" -> SubscriptionStatus.TRAILING;
            case "past_due", "unpaid", "paused", "incomplete_expired" -> SubscriptionStatus.PAST_ONE;
            case "cancelled" -> SubscriptionStatus.CANCELLED;
            case "incomplete" -> SubscriptionStatus.INCOMPLETE;
            default -> {
                log.warn("Unmapped Stripe status: {}", status);
                yield null;
            }
        };

    }

    private Instant toInstant(Long epoch) {

        return epoch!=null ? Instant.ofEpochSecond(epoch) : null;
    }

    private Long resolvePlanId(Price price) {

        if(price==null || price.getId()==null) return null;
        return planRepository.findByStripePriceId(price.getId())
                .map(Plan::getId)
                .orElse(null);
    }

    private String extractSubscriptionId(Invoice invoice) {
        var parent = invoice.getParent();
        if(parent==null) return null;

        var subDetails = parent.getSubscriptionDetails();
        if(subDetails==null) return null;

        return subDetails.getSubscription();
    }
}
