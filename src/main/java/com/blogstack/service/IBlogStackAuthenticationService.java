package com.blogstack.service;

import com.blogstack.beans.request.SignInRequestBean;
import com.blogstack.beans.request.SignUpRequestBean;

import java.util.Optional;

public interface IBlogStackAuthenticationService {

    public Optional<?> signUp(SignUpRequestBean signUpRequestBean);

    public Optional<?> signIn(SignInRequestBean signInRequestBean);

}
