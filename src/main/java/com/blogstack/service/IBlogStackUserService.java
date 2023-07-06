package com.blogstack.service;

import com.blogstack.beans.request.UserRequestBean;
import com.blogstack.entities.BlogStackUser;

import java.util.Optional;

public interface IBlogStackUserService {

    public Optional<?> addUser(UserRequestBean userRequestBean);

    public Optional<?> fetchUserById(String userId);

    public Optional<?> fetchAll(String filterCriteria, String sortCriteria, Integer page, Integer size);

    public Optional<?> updateUser(UserRequestBean userRequestBean);


}
