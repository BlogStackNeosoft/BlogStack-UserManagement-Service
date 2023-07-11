package com.blogstack.mappers.entity.pojo;

import com.blogstack.beans.response.UserResponseBean;
import com.blogstack.entities.BlogStackUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface IBlogStackUserEntityPojoMapper {

    IBlogStackUserEntityPojoMapper INSTANCE = Mappers.getMapper(IBlogStackUserEntityPojoMapper.class);

    @Mappings({
            @Mapping(target = "userId", source = "blogStackUser.bsuUserId"),
            @Mapping(target = "emailId", source = "blogStackUser.bsuEmailId"),
            @Mapping(target = "lastName", source = "blogStackUser.bsuLastName"),
            @Mapping(target = "middleName", source = "blogStackUser.bsuMiddleName"),
            @Mapping(target = "firstName", source = "blogStackUser.bsuFirstName"),
            @Mapping(target = "address", source = "blogStackUser.bsuAddress"),
            @Mapping(target = "gender", source = "blogStackUser.bsuGender"),
            @Mapping(target = "phoneNumber", source = "blogStackUser.bsuPhoneNumber"),
            @Mapping(target = "dateOfBirth", source = "blogStackUser.bsuDateOfBirth"),
            @Mapping(target = "profilePhoto", source = "blogStackUser.bsuProfilePhoto"),
            @Mapping(target = "status", source = "blogStackUser.bsuStatus"),
    })
    public UserResponseBean mapUserMasterEntityPojoMapping(BlogStackUser blogStackUser);

    public static Function<List<BlogStackUser>, List<UserResponseBean>> mapUserMasterEntityListToPojoListMapping = blogStackUserList -> blogStackUserList.stream()
            .map(blogStackUser -> {
                UserResponseBean.UserResponseBeanBuilder userResponseBeanBuilder = UserResponseBean.builder();
                userResponseBeanBuilder.userId(blogStackUser.getBsuUserId())
                        .emailId(blogStackUser.getBsuEmailId())
                        .lastName(blogStackUser.getBsuLastName())
                        .middleName(blogStackUser.getBsuMiddleName())
                        .firstName(blogStackUser.getBsuFirstName())
                        .address(blogStackUser.getBsuAddress())
                        .gender(blogStackUser.getBsuGender())
                        .phoneNumber(blogStackUser.getBsuPhoneNumber())
                        .dateOfBirth(blogStackUser.getBsuDateOfBirth())
                        .profilePhoto(blogStackUser.getBsuProfilePhoto())
                        .status(blogStackUser.getBsuStatus());
                return userResponseBeanBuilder.build();
            }).collect(Collectors.toList());
}