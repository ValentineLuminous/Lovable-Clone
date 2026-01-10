package com.BlueFlare.Lovable.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectFile {
    Long Id;
    Project project;
    String path;
    String minIOObjectKey;
    Instant createdAt;
    Instant updatedAt;

    User createdBy;
    User updatedBy;
}
