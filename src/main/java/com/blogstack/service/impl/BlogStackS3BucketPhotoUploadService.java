package com.blogstack.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.blogstack.beans.response.ServiceResponseBean;
import com.blogstack.commons.BlogStackCommonConstants;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackUser;
import com.blogstack.exceptions.BlogStackDataNotFoundException;
import com.blogstack.mappers.entity.pojo.IBlogStackUserEntityPojoMapper;
import com.blogstack.repository.IBlogStackUserRepository;
import com.blogstack.service.IBlogStackS3BucketPhotoUploadService;
import com.blogstack.service.IBlogStackUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class BlogStackS3BucketPhotoUploadService implements IBlogStackS3BucketPhotoUploadService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Clinet;

    @Autowired
    private IBlogStackUserRepository blogStackUserRepository;

    @Autowired
    private IBlogStackUserService blogStackUserService;

    @Override
    public ResponseEntity<File> convertMultiPartFileToFile(MultipartFile blogStackProfilePhoto) throws FileNotFoundException, IOException {
        FileOutputStream fileOutputStream = null;
        File convertedFile = null;
        try {
            convertedFile = new File(Objects.requireNonNull(blogStackProfilePhoto.getOriginalFilename()));
            fileOutputStream = new FileOutputStream(convertedFile);
            fileOutputStream.write(blogStackProfilePhoto.getBytes());
        } finally {
            assert fileOutputStream != null;
            fileOutputStream.close();
        }
        return ResponseEntity.ok(convertedFile);
    }

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile blogStackProfilePhoto) throws IOException {
        ResponseEntity<File> convertedFile = convertMultiPartFileToFile(blogStackProfilePhoto);
        String fileName = System.currentTimeMillis() + BlogStackCommonConstants.INSTANCE.UNDERSCORE_STRING + blogStackProfilePhoto.getOriginalFilename();
        s3Clinet.putObject(new PutObjectRequest(bucketName, fileName, convertedFile.getBody()));
        URL url = s3Clinet.getUrl(bucketName, fileName);
        return ResponseEntity.ok(url.toString());
    }

    @Override
    public ResponseEntity<ServiceResponseBean> uploadProfilePhoto(String email, MultipartFile blogStackUserProfilePhoto) throws IOException {
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailId(email);
        if (blogStackUserOptional.isEmpty())
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.DATA_NOT_FOUND);

        ResponseEntity<String> blogStackUserProfilePhotoUrl = uploadFile(blogStackUserProfilePhoto);
        blogStackUserOptional.get().setBsuProfilePhoto(blogStackUserProfilePhotoUrl.getBody());

        BlogStackUser blogStackUser = this.blogStackUserRepository.saveAndFlush(blogStackUserOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackUserEntityPojoMapper.INSTANCE.mapUserMasterEntityPojoMapping(blogStackUser)).build());
    }
}
