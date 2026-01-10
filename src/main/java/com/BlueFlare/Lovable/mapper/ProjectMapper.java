package com.BlueFlare.Lovable.mapper;

import com.BlueFlare.Lovable.dto.project.ProjectResponse;
import com.BlueFlare.Lovable.dto.project.ProjectSummaryResponse;
import com.BlueFlare.Lovable.entity.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);

    List<ProjectSummaryResponse> toListOfProjectSummaryResponse(List<Project> projects);
}
