package com.BlueFlare.Lovable.dto.subscription;

public record PlanLimitResponse(
        String planName,
        Integer maxTokenPerDay,
        Integer maxProjects,
        boolean unlimitedAi
) {
}
