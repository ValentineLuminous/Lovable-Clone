package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.subscription.PlanResponse;
import com.BlueFlare.Lovable.entity.Plan;
import com.BlueFlare.Lovable.mapper.PlanMapper;
import com.BlueFlare.Lovable.repository.PlanRepository;
import com.BlueFlare.Lovable.services.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    @Override
    public List<PlanResponse> getAllActivePlans() {

        List<Plan> planList  = planRepository.findAll();
        return planMapper.fromListOfPlans(planList);
    }
}
