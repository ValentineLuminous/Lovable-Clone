package com.BlueFlare.Lovable.controllers;

import com.BlueFlare.Lovable.dto.subscription.PlanLimitResponse;
import com.BlueFlare.Lovable.dto.subscription.UsageTodayResponse;
import com.BlueFlare.Lovable.services.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage")
public class UsageController {
    private final UsageService usageService;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getTodayUsage(){
        Long userId = 1L;

        return ResponseEntity.ok(usageService.getTodayUsage(userId));
    }

    @GetMapping("limits")
    public ResponseEntity<PlanLimitResponse> getPlanLimits(){
        Long userId = 1L;

        return ResponseEntity.ok(usageService.getCurrentSubscriptionLimitsOfUser(userId));
    }
}
