package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.project.FileContentResponse;
import com.BlueFlare.Lovable.dto.project.FileNode;
import com.BlueFlare.Lovable.services.ProjectFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class ProjectFileServiceImpl implements ProjectFileService {
    @Override
    public List<FileNode> getFileTree(Long projectId, Long userId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path, Long userId) {
        return null;
    }

    @Override
    public void saveFile(Long projectId, String filePath, String fileContent) {
        log.info("Saving file: {}", filePath);
        // save the metadata in postgres
        //save the content inside minio

    }
}
