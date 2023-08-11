package com.blogstack.service;

import com.blogstack.beans.request.SignUpRequestBean;
import org.springframework.http.ResponseEntity;

public interface IBlogStackSuperAdminService {
    ResponseEntity<?> addAdmin(SignUpRequestBean adminSignUpRequestBean);
    ResponseEntity<?> getAllAdmins();
}