package com.BlueFlare.Lovable.entity;


import jakarta.persistence.Access;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Plan {
    Long id;
    String name;
    String stripePriceId;
    Integer maxTokenPerDay;
    Integer maxPreviews;
    Boolean unlimitedAi;

    Boolean active;
}
