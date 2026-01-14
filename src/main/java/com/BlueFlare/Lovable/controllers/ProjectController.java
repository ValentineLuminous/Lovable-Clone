package com.BlueFlare.Lovable.controllers;

import com.BlueFlare.Lovable.dto.project.ProjectRequest;
import com.BlueFlare.Lovable.dto.project.ProjectResponse;
import com.BlueFlare.Lovable.dto.project.ProjectSummaryResponse;
import com.BlueFlare.Lovable.security.AuthUtil;
import com.BlueFlare.Lovable.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;


    @GetMapping()
    public ResponseEntity<List<ProjectSummaryResponse>> getMyProjects(){

        return ResponseEntity.ok(projectService.getUserProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id){

        return ResponseEntity.ok(projectService.getUserProjectById(id));
    }

    @PostMapping()
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @RequestBody @Valid ProjectRequest request){

        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){

        projectService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}

