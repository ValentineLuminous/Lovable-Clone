package com.BlueFlare.Lovable.mapper;

import com.BlueFlare.Lovable.dto.auth.SignupRequest;
import com.BlueFlare.Lovable.dto.auth.UserProfileResponse;
import com.BlueFlare.Lovable.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

     User toEntity(SignupRequest signupRequest);
     UserProfileResponse toUserProfileResponse(User user);
}
