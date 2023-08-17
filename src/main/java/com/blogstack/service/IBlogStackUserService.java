package com.blogstack.service;

import com.blogstack.beans.request.UserRequestBean;
import org.springframework.http.ResponseEntity;

public interface IBlogStackUserService {
     ResponseEntity<?> fetchUserById(String userId);
     ResponseEntity<?> fetchAll(Integer page, Integer size);
     ResponseEntity<?> fetchUserByUserId(String userId);
     ResponseEntity<?> updateUser(UserRequestBean userRequestBean);
     ResponseEntity<?> deleteUser(String userId);
     ResponseEntity<?> fetchUserByUserId(String userId);

     ResponseEntity<?> resetPassword(String blogStackUSerEmail, String blogStackUserPassword);

     ResponseEntity<?> fetchAllQuestionByUserId(String userId);
}