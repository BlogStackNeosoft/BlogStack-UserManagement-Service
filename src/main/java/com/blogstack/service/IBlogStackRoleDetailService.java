package com.blogstack.service;

import com.blogstack.beans.request.RoleRequestBean;
import reactor.core.publisher.Mono;

public interface IBlogStackRoleDetailService {

    public Mono<?> addRole(RoleRequestBean roleRequestBean);

    public Mono<?> fetchRoleByRoleId(String roleId);

    public Mono<?> fetchAllRole(String filterCriteria, String sortCriteria, Integer page, Integer size);

    public Mono<?> updateRole(RoleRequestBean roleRequestBean);

    public Mono<?> deleteRole(String roleId);

}
