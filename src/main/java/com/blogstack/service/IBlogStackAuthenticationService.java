package com.blogstack.service;

import com.blogstack.beans.request.SignInRequestBean;
import com.blogstack.beans.request.SignUpRequestBean;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface IBlogStackAuthenticationService {

    public Mono<?> signUp(SignUpRequestBean signUpRequestBean);

    public Mono<?> signIn(SignInRequestBean signInRequestBean);

}
