package com.blogstack.controllers;

import com.blogstack.beans.request.UserRequestBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.service.IBlogStackUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping(path = "${iam-service-version}/user")
@CrossOrigin("*")
public class BlogStackUserController {

    @Autowired
    private IBlogStackUserService blogStackUserService;

    @GetMapping(value = "/{email_id}")
    public Optional<?> fetchUserById(@PathVariable(value = "email_id") @NotBlank(message = BlogStackMessageConstants.EMAIL_CANT_BLANK) String emailId) {
        return this.blogStackUserService.fetchUserById(emailId);
    }

    @GetMapping(value = "/")
    public Optional<?> fetchAllUser(@RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "2147483647") Integer size) {
        return this.blogStackUserService.fetchAll(page, size);
    }

    @PutMapping(value = "/")
    public Optional<?> updateUser(@Valid @RequestBody UserRequestBean userRequestBean) {
        return this.blogStackUserService.updateUser(userRequestBean);
    }

    @DeleteMapping(value = "/{email_id}")
    public Optional<?> deleteUser(@PathVariable(value = "email_id") @NotBlank(message = BlogStackMessageConstants.EMAIL_CANT_BLANK) String emailId) {
        return this.blogStackUserService.deleteUser(emailId);
    }
}