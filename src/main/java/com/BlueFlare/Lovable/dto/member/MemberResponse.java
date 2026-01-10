package com.BlueFlare.Lovable.dto.member;

import com.BlueFlare.Lovable.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String username,
        String name,
        ProjectRole projectRole,
        Instant invitedAt
) {
}
