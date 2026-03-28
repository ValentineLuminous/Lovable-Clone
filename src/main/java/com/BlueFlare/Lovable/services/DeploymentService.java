package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.deploy.DeployResponse;

public interface DeploymentService {

    DeployResponse deploy(Long projectId);
}
