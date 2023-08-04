package com.blogstack.controllers;

import com.blogstack.beans.request.UserRequestBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.service.IBlogStackS3BucketPhotoUploadService;
import com.blogstack.service.IBlogStackUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "${iam-service-version}/user")
// @CrossOrigin("*")
public class BlogStackUserController {

    @Autowired
    private IBlogStackUserService blogStackUserService;

    @Autowired
    private IBlogStackS3BucketPhotoUploadService blogStackS3BucketProfilePhotoUploadService;

    @GetMapping(value = "/{email_id}")
    public ResponseEntity<?> fetchUserById(@PathVariable(value = "email_id") @NotBlank(message = BlogStackMessageConstants.EMAIL_CANT_BLANK) String emailId) {
        return this.blogStackUserService.fetchUserById(emailId);
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> fetchAllUser(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "2147483647") Integer size) {
        return this.blogStackUserService.fetchAll(page, size);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequestBean userRequestBean) {
        return this.blogStackUserService.updateUser(userRequestBean);
    }

    @DeleteMapping(value = "/{email_id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "email_id") @NotBlank(message = BlogStackMessageConstants.EMAIL_CANT_BLANK) String emailId) {
        return this.blogStackUserService.deleteUser(emailId);
    }

    @PutMapping(value = "/profile-photo/{email_id}")
    public ResponseEntity<?> updateProfilePhoto(@PathVariable(value = "email_id") @NotBlank(message = BlogStackMessageConstants.EMAIL_CANT_BLANK) String emailId, @RequestParam(value = "profile_pic")MultipartFile profilePic) throws IOException {
        return this.blogStackS3BucketProfilePhotoUploadService.uploadProfilePhoto(emailId,profilePic);
    }
}