package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.member.InviteMemberrequest;
import com.BlueFlare.Lovable.dto.member.MemberResponse;
import com.BlueFlare.Lovable.dto.member.UpdateMemberRoleRequest;
import com.BlueFlare.Lovable.entity.Project;
import com.BlueFlare.Lovable.entity.ProjectMember;
import com.BlueFlare.Lovable.entity.ProjectMemberId;
import com.BlueFlare.Lovable.entity.User;
import com.BlueFlare.Lovable.mapper.ProjectMemberMapper;
import com.BlueFlare.Lovable.repository.ProjectMemberRepository;
import com.BlueFlare.Lovable.repository.ProjectRepository;
import com.BlueFlare.Lovable.repository.UserRepository;
import com.BlueFlare.Lovable.services.ProjectMemberService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {

    ProjectRepository projectRepository;
    ProjectMemberRepository projectMemberRepository;
    ProjectMemberMapper projectMemberMapper;
    UserRepository userRepository;

    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {

        Project project = getAccessibleProjectById(projectId,userId);
        return projectMemberRepository.findByIdProjectId(projectId)
                .stream()
                .map(projectMemberMapper::toProjectMemberResponseFromMember)
                .toList();
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberrequest request, Long userId) {
        Project project = getAccessibleProjectById(projectId, userId);

//        if(!project.getOwner().getId().equals(userId)){
//            throw new RuntimeException("You're not allowed to invite members");
//        }
        User invitee = userRepository.findByUsername(request.username()).orElseThrow();

        if(invitee.getId().equals(userId)){
            throw new RuntimeException("Cannot invite yourself");
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, invitee.getId());
        if(projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("Cannot invite once again");
        }

        ProjectMember member = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .user(invitee)
                .role(request.role())
                .invitedAt(Instant.now())
                .build();

        projectMemberRepository.save(member);

        return projectMemberMapper.toProjectMemberResponseFromMember(member);
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId) {

        Project project = getAccessibleProjectById(projectId, userId);

//        if(!project.getOwner().getId().equals(userId)){
//            throw new RuntimeException("You're not allowed to invite members");
//        }
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId).orElseThrow();

        projectMember.setRole(request.role());

        projectMemberRepository.save(projectMember);

        return projectMemberMapper.toProjectMemberResponseFromMember(projectMember);
    }

    @Override
    public void  removeProjectMember(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId) {

        Project project = getAccessibleProjectById(projectId, userId);

//        if(!project.getOwner().getId().equals(userId)){
//            throw new RuntimeException("You're not allowed to remove members");
//        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        if(!projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("Member is not present in project");
        }

        projectMemberRepository.deleteById(projectMemberId);

    }

    public Project getAccessibleProjectById(Long projectId, Long userId){
        return projectRepository.findAccessibleProjectById(projectId, userId).orElseThrow();
    }
}
