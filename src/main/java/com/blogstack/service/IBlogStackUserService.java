package com.blogstack.service;

import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.beans.request.UserRequestBean;
import com.blogstack.entities.BlogStackUser;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface IBlogStackUserService {


     Optional<?> fetchUserById(String userId);

     Optional<?> fetchAll(Integer page, Integer size);

     Optional<?> updateUser(UserRequestBean userRequestBean);

     Optional<?> deleteUser(String userId);

     Optional<?> updateUser(BlogStackUser blogStackUser);
}