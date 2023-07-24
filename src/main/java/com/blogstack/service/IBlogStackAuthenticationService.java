package com.blogstack.service;

import com.blogstack.beans.request.SignInRequestBean;
import com.blogstack.beans.request.SignUpRequestBean;

import java.io.IOException;
import java.util.Optional;

public interface IBlogStackAuthenticationService {

    public Optional<?> signUp(SignUpRequestBean signUpRequestBean) throws IOException;

    public Optional<?> signIn(SignInRequestBean signInRequestBean);

    public Optional<?> refreshTokens(String refreshToken);

}
