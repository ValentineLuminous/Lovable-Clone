package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.auth.UserProfileResponse;
import com.BlueFlare.Lovable.error.ResourceNotFoundException;
import com.BlueFlare.Lovable.repository.UserRepository;
import com.BlueFlare.Lovable.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {


    UserRepository userRepository;
    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User", username));
    }
}
