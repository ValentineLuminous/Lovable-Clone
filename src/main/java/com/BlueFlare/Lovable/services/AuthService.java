package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.auth.AuthResponse;
import com.BlueFlare.Lovable.dto.auth.LoginRequest;
import com.BlueFlare.Lovable.dto.auth.SignupRequest;
import com.BlueFlare.Lovable.dto.auth.UserProfileResponse;
//import org.jspecify.annotations.Nullable;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

     AuthResponse login(LoginRequest request);


}
