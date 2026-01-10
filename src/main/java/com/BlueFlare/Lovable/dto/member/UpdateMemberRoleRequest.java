package com.BlueFlare.Lovable.dto.member;

import com.BlueFlare.Lovable.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(
        @NotNull ProjectRole role) {
}
