package com.blogstack.feign.clients;

import com.blogstack.configs.FeignSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@FeignClient(name = "BlogStackCommonsService", url = "http://localhost:9091/v1.0/file-upload", configuration = FeignSupportConfig.class)
public interface IBlogStackUploadFileService {

    @PutMapping(value = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> uploadFile(@RequestPart(name = "file") MultipartFile file, @RequestPart(name = "bucketName") String bucketName) throws IOException;
}