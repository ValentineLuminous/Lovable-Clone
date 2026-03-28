package com.BlueFlare.Lovable.services;

//import org.jspecify.annotations.Nullable;


public interface UsageService {
//    UsageTodayResponse getTodayUsage(Long userId);
//
//    PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId);

    void recordTokenUsage(Long userId, int actualTokens);
    void checkDailyTokensUsage();
}
