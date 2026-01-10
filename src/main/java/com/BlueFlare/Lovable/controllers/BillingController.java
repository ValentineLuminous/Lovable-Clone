package com.BlueFlare.Lovable.controllers;

import com.BlueFlare.Lovable.dto.subscription.CheckoutRequest;
import com.BlueFlare.Lovable.dto.subscription.CheckoutResponse;
import com.BlueFlare.Lovable.dto.subscription.PlanResponse;
import com.BlueFlare.Lovable.dto.subscription.PortalReponse;
import com.BlueFlare.Lovable.dto.subscription.SubscriptionResponse;
import com.BlueFlare.Lovable.services.PlanService;
import com.BlueFlare.Lovable.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class BillingController {
    private final PlanService planService;
    private final SubscriptionService subscriptionService;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanResponse>> getAllActivePlans(){
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription(){
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping("/api/stripe/checkout")
    public ResponseEntity<CheckoutResponse> createCheckOutResponse(@RequestBody CheckoutRequest request){
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.createCheckoutSessionUrl(request, userId));
    }

    @PostMapping("/api/stripe/portal")
    public ResponseEntity<PortalReponse> openCustomerPortal(){
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.openCutomerService(userId));
    }
}
