package com.BlueFlare.Lovable.controllers;

import com.BlueFlare.Lovable.dto.member.InviteMemberrequest;
import com.BlueFlare.Lovable.dto.member.MemberResponse;
import com.BlueFlare.Lovable.dto.member.UpdateMemberRoleRequest;
import com.BlueFlare.Lovable.services.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getProjectMembers(@PathVariable Long projectId){
        Long userId = 1L;
        return ResponseEntity.ok(projectMemberService.getProjectMembers(projectId, userId));
    }

    @PostMapping
    public ResponseEntity<MemberResponse> inviteMember(
            @PathVariable Long projectId,
            @RequestBody InviteMemberrequest request
    ){
        Long userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(projectMemberService.inviteMember(projectId,request, userId));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(@PathVariable Long memberId, @PathVariable Long projectId, @RequestBody UpdateMemberRoleRequest request){
        Long userId = 1L;
        return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId, memberId, request, userId));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeProjectMember(@PathVariable Long memberId, @PathVariable Long projectId, @RequestBody UpdateMemberRoleRequest request){
        Long userId = 1L;
        projectMemberService.removeProjectMember(projectId, memberId, request, userId);
        return ResponseEntity.noContent().build();
    }




}
