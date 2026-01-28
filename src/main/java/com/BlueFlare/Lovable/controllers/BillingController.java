package com.BlueFlare.Lovable.controllers;

import com.BlueFlare.Lovable.dto.subscription.CheckoutRequest;
import com.BlueFlare.Lovable.dto.subscription.CheckoutResponse;
import com.BlueFlare.Lovable.dto.subscription.PlanResponse;
import com.BlueFlare.Lovable.dto.subscription.PortalReponse;
import com.BlueFlare.Lovable.dto.subscription.SubscriptionResponse;
import com.BlueFlare.Lovable.services.PaymentProcessor;
import com.BlueFlare.Lovable.services.PlanService;
import com.BlueFlare.Lovable.services.SubscriptionService;
import com.BlueFlare.Lovable.services.implementation.StripePaymentProcessor;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class BillingController {

    @Value("${stripe.webhook.secret}")
    private String webHookSecret;
    private final PlanService planService;
    private final SubscriptionService subscriptionService;
    private final PaymentProcessor paymentProcessor;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanResponse>> getAllActivePlans(){
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription(){
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping("/api/payment/checkout")
    public ResponseEntity<CheckoutResponse> createCheckOutResponse(@RequestBody CheckoutRequest request){

        return ResponseEntity.ok(paymentProcessor.createCheckOutSessionUrl(request));
    }

    @PostMapping("/api/payment/portal")
    public ResponseEntity<PortalReponse> openCustomerPortal(){
        Long userId = 1L;
        return ResponseEntity.ok(paymentProcessor.openCustomerPortal(userId));
    }

    @PostMapping("/webhooks/payment")
    public ResponseEntity<String> handlePaymentWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signHeader
    ){
        try {
            Event event = Webhook.constructEvent(payload, signHeader, webHookSecret);
            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;

            if(deserializer.getObject().isPresent()){
                stripeObject = deserializer.getObject().get();
            }
            else{

                try{
                    stripeObject = deserializer.deserializeUnsafe();
                    if(stripeObject==null){
                        log.warn("Failed to deserialize webhook object for event: {}", event.getType());
                        return ResponseEntity.ok().build();
                    }
                } catch(Exception e) {
                    log.error("Unsafe desrialization failed for event {}: {}", event.getType(), e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deserialization Failed");
                }
            }

            Map<String, String> metadata = new HashMap<>();
            if(stripeObject instanceof Session session) {
                metadata = session.getMetadata();
            }

            paymentProcessor.handleWebHook(event.getType(), stripeObject, metadata);

            return ResponseEntity.ok().build();
        } catch(SignatureVerificationException e){
            throw new RuntimeException(e);
        }
    }
}
