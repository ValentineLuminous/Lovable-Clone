package com.BlueFlare.Lovable.mapper;

import com.BlueFlare.Lovable.dto.project.FileNode;
import com.BlueFlare.Lovable.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProjectFileMapper {
    List<FileNode> toListOfFileNode(List<ProjectFile> projectFileList);
}
