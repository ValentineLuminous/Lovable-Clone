package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.subscription.CheckoutRequest;
import com.BlueFlare.Lovable.dto.subscription.CheckoutResponse;
import com.BlueFlare.Lovable.dto.subscription.PortalReponse;
import com.BlueFlare.Lovable.entity.Plan;
import com.BlueFlare.Lovable.entity.User;
import com.BlueFlare.Lovable.error.ResourceNotFoundException;
import com.BlueFlare.Lovable.repository.PlanRepository;
import com.BlueFlare.Lovable.repository.UserRepository;
import com.BlueFlare.Lovable.security.AuthUtil;
import com.BlueFlare.Lovable.services.PaymentProcessor;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public PortalReponse openCustomerPortal(Long userId) {
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

        Long userId = Long.parseLong(metadata.get("user_id"));
        Long planId = Long.parseLong(metadata.get("plan_id"));

        String subscriptionId = session.getSubscription();
        String customerId = session.getCustomer();

        User user = getUser(userId);

        if(user.getStripeCustomerId()==null){
            user.setStripeCustomerId(customerId);
            userRepository.save(user);
        }
    }

    private void handleCustomerSubscriptionUpdated(Subscription subscription){

    }
    private void handleCustomerSubscriptionDeleted(Subscription subscription){

    }
    private void handleInvoicePaid(Invoice invoice){

    }
    private void handleInvoicePaymentFailed(Invoice invoice){

    }

    private User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("user", userId.toString()));
    }
}
