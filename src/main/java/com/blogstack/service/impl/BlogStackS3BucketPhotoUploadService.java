package com.blogstack.service.impl;

import com.blogstack.beans.response.ServiceResponseBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackUser;
import com.blogstack.exceptions.BlogStackDataNotFoundException;
import com.blogstack.feign.clients.IBlogStackUploadFileService;
import com.blogstack.mappers.entity.pojo.IBlogStackUserEntityPojoMapper;
import com.blogstack.repository.IBlogStackUserRepository;
import com.blogstack.service.IBlogStackS3BucketPhotoUploadService;
import com.blogstack.service.IBlogStackUserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class BlogStackS3BucketPhotoUploadService implements IBlogStackS3BucketPhotoUploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogStackS3BucketPhotoUploadService.class);

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private IBlogStackUploadFileService blogStackUploadFileService;

    @Autowired
    private IBlogStackUserRepository blogStackUserRepository;

    @Autowired
    private IBlogStackUserService blogStackUserService;

    @Override
    public ResponseEntity<ServiceResponseBean> uploadProfilePhoto(String email, MultipartFile blogStackUserProfilePhoto) throws IOException {
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailId(email);
        if (blogStackUserOptional.isEmpty())
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.DATA_NOT_FOUND);

        ResponseEntity<String> blogStackUserProfilePhotoUrl = this.blogStackUploadFileService.uploadFile(blogStackUserProfilePhoto, bucketName);
        blogStackUserOptional.get().setBsuProfilePhoto(blogStackUserProfilePhotoUrl.getBody());

        BlogStackUser blogStackUser = this.blogStackUserRepository.saveAndFlush(blogStackUserOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackUserEntityPojoMapper.INSTANCE.mapUserMasterEntityPojoMapping(blogStackUser)).build());
    }
}