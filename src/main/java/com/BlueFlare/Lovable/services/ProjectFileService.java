package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.project.FileContentResponse;
import com.BlueFlare.Lovable.dto.project.FileNode;

import java.util.List;

public interface ProjectFileService {
    List<FileNode> getFileTree(Long projectId);

    FileContentResponse getFileContent(Long projectId, String path);

    void saveFile(Long projectId, String filePath, String fileContent);
}
