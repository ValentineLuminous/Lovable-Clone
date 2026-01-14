package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.project.ProjectRequest;
import com.BlueFlare.Lovable.dto.project.ProjectResponse;
import com.BlueFlare.Lovable.dto.project.ProjectSummaryResponse;
//import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getUserProjects();

    ProjectResponse getUserProjectById(Long id);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void softDelete(Long id);
}
