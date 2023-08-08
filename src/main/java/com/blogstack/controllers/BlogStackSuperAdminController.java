package com.blogstack.controllers;

import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.service.IBlogStackSuperAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${iam-service-version}/super-role")
public class BlogStackSuperAdminController {

    @Autowired
    private IBlogStackSuperAdminService blogStackSuperAdminService;

    @PostMapping("/admin")
    public ResponseEntity<?> addAdmin(@Valid @RequestBody SignUpRequestBean adminSignUpRequestBean){
        return this.blogStackSuperAdminService.addAdmin(adminSignUpRequestBean);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles(){
        return this.blogStackSuperAdminService.getAllAdmins();
    }
}
