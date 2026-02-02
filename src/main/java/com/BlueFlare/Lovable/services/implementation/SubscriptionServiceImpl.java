package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.subscription.CheckoutRequest;
import com.BlueFlare.Lovable.dto.subscription.CheckoutResponse;
import com.BlueFlare.Lovable.dto.subscription.PortalReponse;
import com.BlueFlare.Lovable.dto.subscription.SubscriptionResponse;
import com.BlueFlare.Lovable.entity.Plan;
import com.BlueFlare.Lovable.entity.Subscription;
import com.BlueFlare.Lovable.entity.User;
import com.BlueFlare.Lovable.enums.SubscriptionStatus;
import com.BlueFlare.Lovable.error.ResourceNotFoundException;
import com.BlueFlare.Lovable.mapper.SubscriptionMapper;
import com.BlueFlare.Lovable.repository.PlanRepository;
import com.BlueFlare.Lovable.repository.SubscriptionRepository;
import com.BlueFlare.Lovable.repository.UserRepository;
import com.BlueFlare.Lovable.security.AuthUtil;
import com.BlueFlare.Lovable.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    @Override
    public SubscriptionResponse getCurrentSubscription() {

        Long userId = authUtil.getCurrentUserId();

        var currentSubscription =  subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(
                SubscriptionStatus.ACTIVE, SubscriptionStatus.PAST_ONE,
                SubscriptionStatus.TRAILING
        )).orElse( new Subscription());

        return subscriptionMapper.toSubscriptionResponse(currentSubscription);
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId) {

        boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);

        if(exists) return;

        User user = getUser(userId);
        Plan plan = getPlan(planId);

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .status(SubscriptionStatus.INCOMPLETE)
                .build();
        subscriptionRepository.save(subscription);
    }

    @Override
    public void updateSubscription(String subscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {

    }

    @Override
    public void cancelSubscription(String subscriptionId) {

    }

    @Override
    public void renewSubscriptionPeriod(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd) {

        Subscription subscription = getSubscription(gatewaySubscriptionId);

        Instant newStart = periodStart != null ? periodStart : subscription.getCurrentPeriodEnd();
        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);

        if(subscription.getStatus()==SubscriptionStatus.PAST_ONE || subscription.getStatus()==SubscriptionStatus.INCOMPLETE){
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }

        subscriptionRepository.save(subscription);
    }



    @Override
    public void markSubscriptionPastDue(String gatewaySubscriptionId) {

    }





    private User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", userId.toString()));
    }

    private Plan getPlan(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(()-> new ResourceNotFoundException("Plan", planId.toString()));
    }

    private Subscription getSubscription (String gatewaySubscriptionId) {
        return subscriptionRepository.findByStripeSubscriptionId(gatewaySubscriptionId)
                .orElseThrow(()-> new ResourceNotFoundException("subscription", gatewaySubscriptionId));
    }


}
