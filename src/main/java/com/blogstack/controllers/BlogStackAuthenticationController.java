package com.blogstack.controllers;

import com.blogstack.beans.request.SignInRequestBean;
import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.service.IBlogStackAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController

@RequestMapping(path = "${iam-service-version}/authentication")
// @CrossOrigin("*")
public class BlogStackAuthenticationController {

    @Autowired
    private IBlogStackAuthenticationService blogStackAuthenticationService;

    @PostMapping("/sign-up/")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestBean signUpRequestBean) throws IOException {
        return this.blogStackAuthenticationService.signUp(signUpRequestBean);
    }

    @PostMapping(value = "/sign-in/")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequestBean signInRequestBean) {
        return this.blogStackAuthenticationService.signIn(signInRequestBean);
    }

    @PostMapping(value = "/refresh-token/")
    public ResponseEntity<?> refreshToken(@RequestParam("token") String token){
        return this.blogStackAuthenticationService.refreshTokens(token);
    }
}