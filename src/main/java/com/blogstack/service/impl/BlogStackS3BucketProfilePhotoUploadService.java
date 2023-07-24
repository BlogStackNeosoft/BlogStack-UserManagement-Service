package com.blogstack.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.blogstack.service.IBlogStackS3BucketProfilePhotoUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Service
@Slf4j
public class BlogStackS3BucketProfilePhotoUploadService implements IBlogStackS3BucketProfilePhotoUploadService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Clinet;

    @Override
    public Optional<File> convertMultiPartFileToFile(MultipartFile blogStackProfilePhoto) throws FileNotFoundException,IOException {
        File convertedFile = new File(blogStackProfilePhoto.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
        fileOutputStream.write(blogStackProfilePhoto.getBytes());
        fileOutputStream.close();
        return Optional.of(convertedFile);
    }

    @Override
    public Optional<String> uploadFile(MultipartFile blogStackProfilePhoto) throws IOException {
        Optional<File> convertedFile = convertMultiPartFileToFile(blogStackProfilePhoto);
        String fileName = System.currentTimeMillis()+"_"+blogStackProfilePhoto.getOriginalFilename();
        s3Clinet.putObject(new PutObjectRequest(bucketName, fileName, convertedFile.get()));
        URL url = s3Clinet.getUrl(bucketName, fileName);
        return Optional.of(url.toString());
    }
}
