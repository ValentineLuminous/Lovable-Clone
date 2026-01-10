package com.BlueFlare.Lovable.controllers;

import com.BlueFlare.Lovable.dto.auth.AuthResponse;
import com.BlueFlare.Lovable.dto.auth.LoginRequest;
import com.BlueFlare.Lovable.dto.auth.SignupRequest;
import com.BlueFlare.Lovable.dto.auth.UserProfileResponse;
import com.BlueFlare.Lovable.services.AuthService;
import com.BlueFlare.Lovable.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;



    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(){
        Long userId = 1L;
        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
