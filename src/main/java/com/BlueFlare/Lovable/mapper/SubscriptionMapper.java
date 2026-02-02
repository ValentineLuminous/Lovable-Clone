package com.BlueFlare.Lovable.mapper;

import com.BlueFlare.Lovable.dto.subscription.PlanResponse;
import com.BlueFlare.Lovable.dto.subscription.SubscriptionResponse;
import com.BlueFlare.Lovable.entity.Subscription;
import com.BlueFlare.Lovable.entity.Plan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanResponse toPlan(Plan plan);



}
