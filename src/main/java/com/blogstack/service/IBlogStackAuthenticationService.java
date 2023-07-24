package com.blogstack.service;

import com.blogstack.beans.request.SignInRequestBean;
import com.blogstack.beans.request.SignUpRequestBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface IBlogStackAuthenticationService {

    public Optional<?> signUp(SignUpRequestBean signUpRequestBean, MultipartFile profilePhoto) throws IOException;

    public Optional<?> signIn(SignInRequestBean signInRequestBean);

    public Optional<?> refreshTokens(String refreshToken);

}
