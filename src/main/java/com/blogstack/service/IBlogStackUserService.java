package com.blogstack.service;

import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.beans.request.UserRequestBean;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface IBlogStackUserService {


    public Mono<?> fetchUserById(String userId);

    public Mono<?> fetchAll(String filterCriteria, String sortCriteria, Integer page, Integer size);

    public Mono<?> updateUser(UserRequestBean userRequestBean);

    public Mono<?> deleteUser(String userId);
}