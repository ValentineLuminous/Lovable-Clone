package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.auth.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(Long userId);
}
