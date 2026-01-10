package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.subscription.PlanLimitResponse;
import com.BlueFlare.Lovable.dto.subscription.UsageTodayResponse;
import com.BlueFlare.Lovable.services.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {
    @Override
    public UsageTodayResponse getTodayUsage(Long userId) {
        return null;
    }

    @Override
    public PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId) {
        return null;
    }
}
