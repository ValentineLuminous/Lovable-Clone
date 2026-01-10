package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.member.InviteMemberrequest;
import com.BlueFlare.Lovable.dto.member.MemberResponse;
import com.BlueFlare.Lovable.dto.member.UpdateMemberRoleRequest;
//import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberrequest request, Long userId);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId);

    void removeProjectMember(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId);
}
