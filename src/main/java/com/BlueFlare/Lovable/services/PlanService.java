package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.subscription.PlanResponse;
//import org.jspecify.annotations.Nullable;

import java.util.List;

public interface PlanService {
    List<PlanResponse> getAllActivePlans();
}
