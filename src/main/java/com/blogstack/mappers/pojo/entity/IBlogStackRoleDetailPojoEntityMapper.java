package com.blogstack.mappers.pojo.entity;

import com.blogstack.beans.request.RoleRequestBean;
import com.blogstack.entities.BlogStackRoleDetail;
import com.blogstack.enums.RoleStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, RoleStatusEnum.class})
public interface IBlogStackRoleDetailPojoEntityMapper {

    IBlogStackRoleDetailPojoEntityMapper INSTANCE = Mappers.getMapper(IBlogStackRoleDetailPojoEntityMapper.class);
    @Mappings({
            @Mapping(target = "brdRoleId", source = "roleRequestBean.roleId"),
            @Mapping(target = "brdRoleName",source = "roleRequestBean.roleName"),
            @Mapping(target = "brdStatus",source = "roleRequestBean.status"),
            @Mapping(target = "brdCreatedBy",source = "roleRequestBean.createdBy"),
            @Mapping(target = "brdCreatedDate",expression = "java(LocalDateTime.now())")
    })
    BlogStackRoleDetail rolePojoToRoleEntity(RoleRequestBean roleRequestBean);

    public static BiFunction<RoleRequestBean, BlogStackRoleDetail, BlogStackRoleDetail> updateRole = (roleRequestBean, blogStackRoleDetail) -> {
        blogStackRoleDetail.setBrdRoleId(roleRequestBean.getRoleId() != null ? roleRequestBean.getRoleId() : blogStackRoleDetail.getBrdRoleId());
        blogStackRoleDetail.setBrdRoleName(roleRequestBean.getRoleName() != null ? roleRequestBean.getRoleName() : blogStackRoleDetail.getBrdRoleName());
        blogStackRoleDetail.setBrdStatus(roleRequestBean.getStatus() != null ? roleRequestBean.getStatus() : blogStackRoleDetail.getBrdStatus());
        blogStackRoleDetail.setBrdModifiedBy(roleRequestBean.getModifiedBy() != null ? roleRequestBean.getModifiedBy() : blogStackRoleDetail.getBrdModifiedBy());
        blogStackRoleDetail.setBrdModifiedDate(LocalDateTime.now());
        return blogStackRoleDetail;
    };
}

