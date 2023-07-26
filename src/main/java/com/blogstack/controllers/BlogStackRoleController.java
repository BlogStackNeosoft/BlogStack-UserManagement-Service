package com.blogstack.controllers;

import com.blogstack.beans.request.RoleRequestBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.service.IBlogStackRoleDetailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "${iam-service-version}/role")
@CrossOrigin("*")
public class BlogStackRoleController {

    @Autowired
    private IBlogStackRoleDetailService blogStackRoleDetailService;

    @PostMapping(value = "/")
    public ResponseEntity<?> addRole(@Valid @RequestBody RoleRequestBean roleRequestBean) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.blogStackRoleDetailService.addRole(roleRequestBean));
    }

    @GetMapping(value = "/{role_name}")
    public ResponseEntity<?> fetchRoleById(@PathVariable(value = "role_name") @NotBlank(message = BlogStackMessageConstants.ROLE_NAME_CANT_BLANK) String roleName) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.blogStackRoleDetailService.fetchRoleByRoleName(roleName));
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> fetchAllRole(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "2147483647") Integer size) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.blogStackRoleDetailService.fetchAllRole(page, size));
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateRole(@Valid @RequestBody RoleRequestBean roleRequestBean) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.blogStackRoleDetailService.updateRole(roleRequestBean));
    }

    @DeleteMapping(value = "/{role_name}")
    public ResponseEntity<?> deleteRole(@PathVariable(value = "role_name") @NotBlank(message = BlogStackMessageConstants.ROLE_ID_CANT_EMPTY) String roleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.blogStackRoleDetailService.deleteRole(roleId));
    }

}
