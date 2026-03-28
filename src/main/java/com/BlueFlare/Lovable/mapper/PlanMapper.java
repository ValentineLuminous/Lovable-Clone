package com.BlueFlare.Lovable.mapper;

import com.BlueFlare.Lovable.dto.subscription.PlanResponse;
import com.BlueFlare.Lovable.entity.Plan;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanMapper {
    List<PlanResponse> fromListOfPlans(List<Plan> planList);
}
