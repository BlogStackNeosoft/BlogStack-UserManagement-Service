package com.blogstack.mappers.pojo.entity;

import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.beans.request.UserRequestBean;
import com.blogstack.entities.BlogStackUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface IBlogStackUserPojoEntityMapper {

    IBlogStackUserPojoEntityMapper INSTANCE = Mappers.getMapper(IBlogStackUserPojoEntityMapper.class);
    @Mappings({
            @Mapping(target = "bsuUserId", source = "signUpRequestBean.userId"),
            @Mapping(target = "bsuEmailId", source = "signUpRequestBean.emailId"),
            @Mapping(target = "bsuLastName", source = "signUpRequestBean.lastName"),
            @Mapping(target = "bsuMiddleName", source = "signUpRequestBean.middleName"),
            @Mapping(target = "bsuFirstName", source = "signUpRequestBean.firstName"),
            @Mapping(target = "bsuAddress", source = "signUpRequestBean.address"),
            @Mapping(target = "bsuGender",source = "signUpRequestBean.gender"),
            @Mapping(target = "bsuPhoneNumber", source = "signUpRequestBean.phoneNumber"),
            @Mapping(target = "bsuDateOfBirth", source = "signUpRequestBean.dateOfBirth"),
            @Mapping(target = "bsuPassword", source = "signUpRequestBean.password"),
            @Mapping(target = "blogStackRoleDetails", source = "signUpRequestBean.blogStackRoleDetails"),
            @Mapping(target = "bsuStatus", source = "signUpRequestBean.status"),
            @Mapping(target = "bsuCreatedBy", source = "signUpRequestBean.createdBy"),
            @Mapping(target = "bsuJwtSecret", source = "signUpRequestBean.bsuJwtSecret"),
            @Mapping(target = "bsuCreatedDate", expression = "java(LocalDateTime.now())")
    })
    public BlogStackUser userPojoToUserEntity(SignUpRequestBean signUpRequestBean);

    public static BiFunction<UserRequestBean, BlogStackUser, BlogStackUser> updateUser = (userRequestBean, blogstackUser) -> {
        blogstackUser.setBsuUserId(userRequestBean.getUserId() != null ? userRequestBean.getUserId() : blogstackUser.getBsuUserId());
        blogstackUser.setBsuEmailId(userRequestBean.getEmailId() != null ? userRequestBean.getEmailId() : blogstackUser.getBsuEmailId());
        blogstackUser.setBsuLastName(userRequestBean.getLastName() != null ? userRequestBean.getLastName() : blogstackUser.getBsuLastName());
        blogstackUser.setBsuMiddleName(userRequestBean.getMiddleName() != null ? userRequestBean.getMiddleName() : blogstackUser.getBsuMiddleName());
        blogstackUser.setBsuFirstName(userRequestBean.getFirstName() != null ? userRequestBean.getFirstName() : blogstackUser.getBsuFirstName());
        blogstackUser.setBsuAddress(userRequestBean.getAddress() != null ? userRequestBean.getAddress() : blogstackUser.getBsuAddress());
        blogstackUser.setBsuGender(userRequestBean.getGender() != null ? userRequestBean.getGender() : blogstackUser.getBsuGender());
        blogstackUser.setBsuDateOfBirth(userRequestBean.getDateOfBirth() != null ? userRequestBean.getDateOfBirth() : blogstackUser.getBsuDateOfBirth());
        blogstackUser.setBsuProfilePhoto(userRequestBean.getProfilePhoto() != null ? userRequestBean.getProfilePhoto() : blogstackUser.getBsuProfilePhoto());
        blogstackUser.setBsuStatus(userRequestBean.getStatus() != null ? userRequestBean.getStatus() : blogstackUser.getBsuStatus());
        blogstackUser.setBsuModifiedBy(userRequestBean.getModifiedBy() != null ? userRequestBean.getModifiedBy() : blogstackUser.getBsuModifiedBy());
        blogstackUser.setBsuModifiedDate(LocalDateTime.now());
        return blogstackUser;
    };
}