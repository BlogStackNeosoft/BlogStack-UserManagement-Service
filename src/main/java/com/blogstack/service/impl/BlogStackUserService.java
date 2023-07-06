package com.blogstack.service.impl;

import com.blogstack.beans.request.UserRequestBean;
import com.blogstack.service.IBlogStackUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class BlogStackUserService implements IBlogStackUserService {

    @Override
    public Optional<?> addUser(UserRequestBean userRequestBean) {
        return Optional.empty();
    }

    @Override
    public Optional<?> fetchUserById(String userId) {
        return Optional.empty();
    }

    @Override
    public Optional<?> fetchAll(String filterCriteria, String sortCriteria, Integer page, Integer size) {
        return Optional.empty();
    }

    @Override
    public Optional<?> updateUser(UserRequestBean userRequestBean) {
        return Optional.empty();
    }
}
