package com.blogstack.controllers;

import com.blogstack.beans.request.UserRequestBean;
import com.blogstack.commons.MessageCodeConstants;
import com.blogstack.service.IBlogStackUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "${iam-service-version}/user")
@CrossOrigin("*")
public class BlogStackUserController {

    @Autowired
    private IBlogStackUserService blogStackUserService;

    @GetMapping(value = "/{user_id}")
    public Mono<?> fetchUserById(@PathVariable(value = "user_id") @NotBlank(message = MessageCodeConstants.USER_ID_CANT_EMPTY) String userId) {
        return this.blogStackUserService.fetchUserById(userId);
    }

    @GetMapping(value = "/")
    public Mono<?> fetchAllUser(@RequestParam(value = "filter_criteria", required = false) String filterCriteria,
                                @RequestParam(value = "sort_criteria", required = false) String sortCriteria,
                                @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "2147483647") Integer size) {
        return this.blogStackUserService.fetchAll(filterCriteria, sortCriteria, page, size);
    }

    @PutMapping(value = "/")
    public Mono<?> updateUser(@Valid @RequestBody UserRequestBean userRequestBean) {
        return this.blogStackUserService.updateUser(userRequestBean);
    }

    @DeleteMapping(value = "/{user_id}")
    public Mono<?> deleteUser(@PathVariable(value = "user_id") @NotBlank(message = MessageCodeConstants.USER_ID_CANT_EMPTY) String userId) {
        return this.blogStackUserService.deleteUser(userId);
    }
}