package com.blogstack.controllers;

import com.blogstack.beans.request.RoleRequestBean;
import com.blogstack.commons.MessageCodeConstants;
import com.blogstack.service.IBlogStackRoleDetailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "${iam-service-version}/role")
@CrossOrigin("*")
public class BlogStackRoleController {

    @Autowired
    private IBlogStackRoleDetailService blogStackRoleDetailService;

    @PostMapping(value = "/")
    public Mono<?> addRole(@Valid @RequestBody RoleRequestBean roleRequestBean) {
        return this.blogStackRoleDetailService.addRole(roleRequestBean);
    }

    @GetMapping(value = "/{role_name}")
    public Mono<?> fetchRoleById(@PathVariable(value = "role_name") @NotBlank(message = MessageCodeConstants.ROLE_ID_CANT_EMPTY) String roleId) {
        return this.blogStackRoleDetailService.fetchRoleByRoleId(roleId);
    }

    @GetMapping(value = "/")
    public Mono<?> fetchAllRole(@RequestParam(value = "filter_criteria", required = false) String filterCriteria,
                                @RequestParam(value = "sort_criteria", required = false) String sortCriteria,
                                @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "2147483647") Integer size) {
        return this.blogStackRoleDetailService.fetchAllRole(filterCriteria, sortCriteria, page, size);
    }

    @PutMapping(value = "/")
    public Mono<?> updateRole(@Valid @RequestBody RoleRequestBean roleRequestBean) {
        return this.blogStackRoleDetailService.updateRole(roleRequestBean);
    }

    @DeleteMapping(value = "/{role_name}")
    public Mono<?> deleteRole(@PathVariable(value = "role_name") @NotBlank(message = MessageCodeConstants.ROLE_ID_CANT_EMPTY) String roleId) {
        return this.blogStackRoleDetailService.deleteRole(roleId);
    }

}
