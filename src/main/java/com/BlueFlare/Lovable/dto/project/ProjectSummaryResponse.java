package com.BlueFlare.Lovable.dto.project;

import com.BlueFlare.Lovable.enums.ProjectRole;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt,
        ProjectRole role
) {
}
