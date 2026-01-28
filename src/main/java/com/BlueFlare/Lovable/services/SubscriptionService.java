package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.subscription.CheckoutRequest;
import com.BlueFlare.Lovable.dto.subscription.CheckoutResponse;
import com.BlueFlare.Lovable.dto.subscription.PortalReponse;
import com.BlueFlare.Lovable.dto.subscription.SubscriptionResponse;
//import org.jspecify.annotations.Nullable;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription(Long userId);

   }
