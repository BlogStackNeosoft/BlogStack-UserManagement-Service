package com.blogstack.service;

import com.blogstack.beans.request.SignInRequestBean;
import com.blogstack.beans.request.SignUpRequestBean;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface IBlogStackAuthenticationService {

    public ResponseEntity<?> signUp(SignUpRequestBean signUpRequestBean) throws IOException;

    public ResponseEntity<?> signIn(SignInRequestBean signInRequestBean);

    public ResponseEntity<?> refreshTokens(String refreshToken);

}
