package com.BlueFlare.Lovable.entity;

import com.BlueFlare.Lovable.enums.SubscriptionStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription {
    Long Id;
    SubscriptionStatus status;
    User user;
    Plan plan;
//    String stripeCustomerId;
    String stripeSubscriptionId;
    Instant currentPeriodStart;
    Instant currentPeriodEnd;
    Boolean cancelPeriodEnd = false ;

    Instant createdAt;
    Instant updatedAt;
}
