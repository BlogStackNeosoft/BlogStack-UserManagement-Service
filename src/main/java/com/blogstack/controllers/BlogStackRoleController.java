package com.blogstack.controllers;

import com.blogstack.beans.request.RoleRequestBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.service.IBlogStackRoleDetailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "${iam-service-version}/role")
@CrossOrigin("*")
public class BlogStackRoleController {

    @Autowired
    private IBlogStackRoleDetailService blogStackRoleDetailService;

    @PostMapping(value = "/")
    public Optional<?> addRole(@Valid @RequestBody RoleRequestBean roleRequestBean) {
        return this.blogStackRoleDetailService.addRole(roleRequestBean);
    }

    @GetMapping(value = "/{role_name}")
    public Optional<?> fetchRoleById(@PathVariable(value = "role_name") @NotBlank(message = BlogStackMessageConstants.ROLE_NAME_CANT_BLANK) String roleName) {
        return this.blogStackRoleDetailService.fetchRoleByRoleName(roleName);
    }

    @GetMapping(value = "/")
    public Optional<?> fetchAllRole(@RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "2147483647") Integer size) {
        return this.blogStackRoleDetailService.fetchAllRole(page, size);
    }

    @PutMapping(value = "/")
    public Optional<?> updateRole(@Valid @RequestBody RoleRequestBean roleRequestBean) {
        return this.blogStackRoleDetailService.updateRole(roleRequestBean);
    }

    @DeleteMapping(value = "/{role_name}")
    public Optional<?> deleteRole(@PathVariable(value = "role_name") @NotBlank(message = BlogStackMessageConstants.ROLE_ID_CANT_EMPTY) String roleId) {
        return this.blogStackRoleDetailService.deleteRole(roleId);
    }

}
