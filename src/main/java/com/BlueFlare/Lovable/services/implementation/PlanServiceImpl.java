package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.subscription.PlanResponse;
import com.BlueFlare.Lovable.services.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Override
    public List<PlanResponse> getAllActivePlans() {
        return List.of();
    }
}
