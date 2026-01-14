package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.project.ProjectRequest;
import com.BlueFlare.Lovable.dto.project.ProjectResponse;
import com.BlueFlare.Lovable.dto.project.ProjectSummaryResponse;
import com.BlueFlare.Lovable.entity.Project;
import com.BlueFlare.Lovable.entity.ProjectMember;
import com.BlueFlare.Lovable.entity.ProjectMemberId;
import com.BlueFlare.Lovable.entity.User;
import com.BlueFlare.Lovable.enums.ProjectRole;
import com.BlueFlare.Lovable.error.ResourceNotFoundException;
import com.BlueFlare.Lovable.mapper.ProjectMapper;
import com.BlueFlare.Lovable.repository.ProjectMemberRepository;
import com.BlueFlare.Lovable.repository.ProjectRepository;
import com.BlueFlare.Lovable.repository.UserRepository;
import com.BlueFlare.Lovable.security.AuthUtil;
import com.BlueFlare.Lovable.services.ProjectService;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final ProjectMemberRepository projectMemberRepository;
    private final AuthUtil authUtil;

    @Override
    public List<ProjectSummaryResponse> getUserProjects() {
          Long userId = authUtil.getCurrentUserId();
//        return projectRepository.findAllAccessibleByUser(userId)
//                .stream()
//                .map(projectMapper::toProjectSummaryResponse)
//                .collect(Collectors.toList());

        var projects = projectRepository.findAllAccessibleByUser(userId);
        return projectMapper.toListOfProjectSummaryResponse(projects);
    }

    @Override
    public ProjectResponse getUserProjectById(Long id) {
        Long userId = authUtil.getCurrentUserId();

        Project project = getAccessibleProject(id, userId);
        return projectMapper.toProjectResponse(project);

    }

    @Override
    public ProjectResponse createProject(ProjectRequest request) {

        Long userId = authUtil.getCurrentUserId();

//        This is making db call which is not required at this point of time
//        User owner = userRepository.findById(userId).orElseThrow(
//                () -> new ResourceNotFoundException("User", userId.toString())
//        );


        //This is not making any db call
        User owner = userRepository.getReferenceById(userId);

        Project project = Project.builder()
                .name(request.name())
                .isPublic(false)
                .build();

        project = projectRepository.save(project);
        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), owner.getId());
        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .role(ProjectRole.OWNER)
                .user(owner)
                .project(project)
                .acceptedAt(Instant.now())
                .invitedAt(Instant.now())
                .build();

        projectMemberRepository.save(projectMember);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request) {

        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProject(id, userId);

//        if(!project.getOwner().getId().equals(userId)){
//            throw new RuntimeException("You are not allowed to update the project");
//        }
        project.setName(request.name());
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long id) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProject(id, userId);
//        if(!project.getOwner().getId().equals(userId)){
//            throw new RuntimeException("You are not allowed to delete");
//        }

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }

    public Project getAccessibleProject(Long projectId, Long userId){
        return projectRepository.findAccessibleProjectById(projectId, userId)
                .orElseThrow(()-> new ResourceNotFoundException("Project", projectId.toString()));
    }
}
