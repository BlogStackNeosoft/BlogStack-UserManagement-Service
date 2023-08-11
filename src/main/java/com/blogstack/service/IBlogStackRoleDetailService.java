package com.blogstack.service;

import com.blogstack.beans.request.RoleRequestBean;
import org.springframework.http.ResponseEntity;

public interface IBlogStackRoleDetailService {
    public ResponseEntity<?> addRole(RoleRequestBean roleRequestBean);
    public ResponseEntity<?> fetchRoleByRoleName(String roleName);
    public ResponseEntity<?> fetchAllRole(Integer page, Integer size);
    public ResponseEntity<?> updateRole(RoleRequestBean roleRequestBean);
    public ResponseEntity<?> deleteRole(String roleId);
}