package com.BlueFlare.Lovable.dto.member;
import com.BlueFlare.Lovable.enums.ProjectRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteMemberrequest(
        @Email @NotBlank String username,
       @NotNull ProjectRole role)
{

}
