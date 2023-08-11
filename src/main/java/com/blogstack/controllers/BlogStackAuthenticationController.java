package com.blogstack.controllers;

import com.blogstack.beans.redis.BlogStackForgotPasswordBean;
import com.blogstack.beans.request.SignInRequestBean;
import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.service.IBlogStackAuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "${iam-service-version}/authentication")
@Slf4j

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

    @PostMapping(value = "/forgot-password/")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String blogStackUserEmail) {
        return this.blogStackAuthenticationService.forgotPasswordEmailGeneration(blogStackUserEmail);
    }

    @PostMapping(value = "/validate-otp/")
    public ResponseEntity<?> validateOtp(@RequestBody BlogStackForgotPasswordBean forgotPasswordBean) {
        return this.blogStackAuthenticationService.blogStackValidateOtp(forgotPasswordBean);
    }

    @PatchMapping(value = "/reset-password/")
    public ResponseEntity<?> sertPassword(@RequestParam("email") String blogStackUserEmail, @RequestParam("password") String blogStackUserPassword){
        return this.blogStackAuthenticationService.resetPassword(blogStackUserEmail,blogStackUserPassword);
    }
}