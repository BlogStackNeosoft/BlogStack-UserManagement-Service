package com.blogstack.controllers;

import com.blogstack.beans.request.SignInRequestBean;
import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.service.IBlogStackAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "${iam-service-version}/authentication/")
public class AuthenticationController {

    @Autowired
    private IBlogStackAuthenticationService blogStackAuthenticationService;

    @PostMapping(value = "/sign-up/")
    public Mono<?> signUp(@Valid @RequestBody SignUpRequestBean signUpRequestBean) {
        return this.blogStackAuthenticationService.signUp(signUpRequestBean);
    }

    @PostMapping(value = "/sign-in/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> signIn(@Valid @RequestBody SignInRequestBean signInRequestBean) {
        return this.blogStackAuthenticationService.signIn(signInRequestBean);
    }

}
