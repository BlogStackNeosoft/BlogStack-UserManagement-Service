package com.blogstack.service;

import com.blogstack.beans.request.RoleRequestBean;

import java.util.Optional;

public interface IBlogStackRoleDetailService {

    public Optional<?> addRole(RoleRequestBean roleRequestBean);

    public Optional<?> fetchRoleByRoleName(String roleName);

    public Optional<?> fetchAllRole(Integer page, Integer size);

    public Optional<?> updateRole(RoleRequestBean roleRequestBean);

    public Optional<?> deleteRole(String roleId);

}
