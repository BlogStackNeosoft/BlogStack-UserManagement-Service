package com.blogstack.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public interface IBlogStackS3BucketProfilePhotoUploadService {

    Optional<File> convertMultiPartFileToFile(MultipartFile file)throws FileNotFoundException, IOException;

    Optional<String> uploadFile(MultipartFile file) throws IOException;
}
