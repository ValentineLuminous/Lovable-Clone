package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.auth.AuthResponse;
import com.BlueFlare.Lovable.dto.auth.LoginRequest;
import com.BlueFlare.Lovable.dto.auth.SignupRequest;
import com.BlueFlare.Lovable.entity.User;
import com.BlueFlare.Lovable.error.BadRequestException;
import com.BlueFlare.Lovable.mapper.UserMapper;
import com.BlueFlare.Lovable.repository.UserRepository;
import com.BlueFlare.Lovable.security.AuthUtil;
import com.BlueFlare.Lovable.services.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthUtil authUtil;
    AuthenticationManager authenticationManager;

    @Override
    public AuthResponse signup(SignupRequest request) {
        userRepository.findByUsername(request.username())
                .ifPresent(user->{
                    throw new BadRequestException("User already with username "+request.username()+" in database");
                });

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);

        String token = authUtil.generateAccessToken(user);


        return new AuthResponse(token, userMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);

        return new AuthResponse(token, userMapper.toUserProfileResponse(user));
    }
}
