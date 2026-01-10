package com.BlueFlare.Lovable.mapper;

import com.BlueFlare.Lovable.dto.member.MemberResponse;
import com.BlueFlare.Lovable.entity.ProjectMember;
import com.BlueFlare.Lovable.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {


    @Mapping(target = "userId", source = "id")
    @Mapping(target = "projectRole", constant = "OWNER")
    MemberResponse toProjectMemberResponseFromOwner(User user);



    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "name", source = "user.name" )
    MemberResponse toProjectMemberResponseFromMember(ProjectMember projectMember);

}
