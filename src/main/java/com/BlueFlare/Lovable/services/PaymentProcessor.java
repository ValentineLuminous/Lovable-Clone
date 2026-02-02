package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.subscription.CheckoutRequest;
import com.BlueFlare.Lovable.dto.subscription.CheckoutResponse;
import com.BlueFlare.Lovable.dto.subscription.PortalReponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentProcessor {
    CheckoutResponse createCheckOutSessionUrl(CheckoutRequest checkoutRequest);

    PortalReponse openCustomerPortal();

    void handleWebHook(String type, StripeObject stripeObject, Map<String, String> metadata);
}
