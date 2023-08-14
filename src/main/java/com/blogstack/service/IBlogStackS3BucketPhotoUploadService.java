package com.blogstack.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IBlogStackS3BucketPhotoUploadService {
    ResponseEntity<?> uploadProfilePhoto(String email, MultipartFile blogStackUserProfilePhoto) throws IOException;
}