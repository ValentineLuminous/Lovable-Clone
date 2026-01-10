package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.auth.UserProfileResponse;
import com.BlueFlare.Lovable.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }
}
