package com.blogstack.mappers.entity.pojo;

import com.blogstack.beans.response.RoleResponseBean;
import com.blogstack.entities.BlogStackRoleDetail;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IBlogStackRoleDetailEntityPojoMapper {

    public static Function<BlogStackRoleDetail, RoleResponseBean> mapRoleEntityPojoMapping = blogStackRoleDetail -> RoleResponseBean.builder()
            .roleId(blogStackRoleDetail.getBrdRoleId())
            .roleName(blogStackRoleDetail.getBrdRoleName())
            .status(blogStackRoleDetail.getBrdStatus())
            .build();

    public static Function<List<BlogStackRoleDetail>, List<RoleResponseBean>> mapRoleEntityListtoPojoListMapping = blogStackRoleDetailList -> blogStackRoleDetailList.stream()
            .map(blogStackRoleDetail -> {
                RoleResponseBean.RoleResponseBeanBuilder roleResponseBeanBuilder = RoleResponseBean.builder();
                roleResponseBeanBuilder.roleId(blogStackRoleDetail.getBrdRoleId())
                        .roleName(blogStackRoleDetail.getBrdRoleName())
                        .status(blogStackRoleDetail.getBrdStatus());
                return roleResponseBeanBuilder.build();
            }).collect(Collectors.toList());
}

