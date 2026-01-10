package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.auth.AuthResponse;
import com.BlueFlare.Lovable.dto.auth.LoginRequest;
import com.BlueFlare.Lovable.dto.auth.SignupRequest;
import com.BlueFlare.Lovable.entity.User;
import com.BlueFlare.Lovable.error.BadRequestException;
import com.BlueFlare.Lovable.mapper.UserMapper;
import com.BlueFlare.Lovable.repository.UserRepository;
import com.BlueFlare.Lovable.services.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    @Override
    public AuthResponse signup(SignupRequest request) {
        userRepository.findByUsername(request.username())
                .ifPresent(user->{
                    throw new BadRequestException("User already with username "+request.username()+" in database");
                });

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);

        return new AuthResponse("Dummy", userMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
