package com.blogstack.controllers;

import com.blogstack.beans.request.SignInRequestBean;
import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.service.IBlogStackAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController

@RequestMapping(path = "${iam-service-version}/authentication")
@CrossOrigin("*")
public class BlogStackAuthenticationController {

    @Autowired
    private IBlogStackAuthenticationService blogStackAuthenticationService;

    @PostMapping("/sign-up/")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestBean signUpRequestBean) throws IOException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.blogStackAuthenticationService.signUp(signUpRequestBean));
    }

    @PostMapping(value = "/sign-in/")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequestBean signInRequestBean) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.blogStackAuthenticationService.signIn(signInRequestBean));
    }

    @PostMapping(value = "/refresh-token/{token}")
    public ResponseEntity<?> refreshToken(@PathVariable String token){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.blogStackAuthenticationService.refreshTokens(token));
    }
}