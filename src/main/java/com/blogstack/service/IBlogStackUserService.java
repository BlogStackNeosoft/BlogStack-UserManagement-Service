package com.blogstack.service;

import com.blogstack.beans.request.UserRequestBean;
import org.springframework.http.ResponseEntity;

public interface IBlogStackUserService {
     ResponseEntity<?> fetchUserById(String userId);
     ResponseEntity<?> fetchAll(Integer page, Integer size);
     ResponseEntity<?> updateUser(UserRequestBean userRequestBean);
     ResponseEntity<?> deleteUser(String userId);

}