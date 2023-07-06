package com.blogstack.controllers;

import com.blogstack.beans.request.UserRequestBean;
import com.blogstack.commons.LocaleMessageCodeConstants;
import com.blogstack.service.IBlogStackUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "${iam-service-version}/user")
public class UserController {

    @Autowired
    private IBlogStackUserService blogStackUserService;

    @PostMapping(value = "/")
    public Optional<?> addUser(@Valid @RequestBody UserRequestBean userRequestBean) {
        return this.blogStackUserService.addUser(userRequestBean);
    }

    @GetMapping(value = "/{user_id}")
    public Optional<?> fetchUserById(@PathVariable(value = "user_id") @NotBlank(message = LocaleMessageCodeConstants.USER_ID_CANT_EMPTY) String userId) {
        return this.blogStackUserService.fetchUserById(userId);
    }

    @GetMapping(value = "/")
    public Optional<?> fetchAllUser(@RequestParam(value = "filter_criteria", required = false) String filterCriteria,
                                @RequestParam(value = "sort_criteria", required = false) String sortCriteria,
                                @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "2147483647") Integer size) {
        return this.blogStackUserService.fetchAll(filterCriteria, sortCriteria, page, size);
    }

    @PutMapping(value = "/")
    public Optional<?> updateUser(@Valid @RequestBody UserRequestBean userRequestBean) {
        return this.blogStackUserService.updateUser(userRequestBean);
    }

}
