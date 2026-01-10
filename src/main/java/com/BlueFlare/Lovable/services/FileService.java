package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.project.FileContentResponse;
import com.BlueFlare.Lovable.dto.project.FileNode;

import java.util.List;

public interface FileService {
    List<FileNode> getFileTree(Long projectId, Long userId);

    FileContentResponse getFileContent(Long projectId, String path, Long userId);
}
