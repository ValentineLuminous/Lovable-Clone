package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.subscription.UsageTodayResponse;
//import org.jspecify.annotations.Nullable;
import com.BlueFlare.Lovable.dto.subscription.PlanLimitResponse;


public interface UsageService {
    UsageTodayResponse getTodayUsage(Long userId);

    PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId);
}
