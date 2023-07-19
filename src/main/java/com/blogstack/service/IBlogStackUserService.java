package com.blogstack.service;

import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.beans.request.UserRequestBean;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface IBlogStackUserService {


    public Optional<?> fetchUserById(String userId);

    public Optional<?> fetchAll(Integer page, Integer size);

    public Optional<?> updateUser(UserRequestBean userRequestBean);

    public Optional<?> deleteUser(String userId);
}