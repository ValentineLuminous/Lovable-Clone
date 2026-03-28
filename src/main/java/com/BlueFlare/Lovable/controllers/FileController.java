package com.BlueFlare.Lovable.controllers;

import com.BlueFlare.Lovable.dto.project.FileContentResponse;
import com.BlueFlare.Lovable.dto.project.FileTreeResponse;
import com.BlueFlare.Lovable.services.ProjectFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/files")
public class FileController {
    private final ProjectFileService projectFileService;

    @GetMapping
    public ResponseEntity<FileTreeResponse> getFileTree(@PathVariable Long projectId){
//        Long userId = 1L;
        return ResponseEntity.ok(projectFileService.getFileTree(projectId));
    }

    @GetMapping("/content")
    public ResponseEntity<FileContentResponse> getFile(
            @PathVariable Long projectId, @RequestParam String path){
//        Long userId = 1L;
        return ResponseEntity.ok(projectFileService.getFileContent(projectId, path));
    }
}
