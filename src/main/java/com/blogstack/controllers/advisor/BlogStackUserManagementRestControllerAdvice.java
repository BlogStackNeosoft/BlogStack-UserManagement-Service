package com.blogstack.controllers.advisor;

import com.blogstack.beans.response.ServiceResponseBean;
import com.blogstack.exceptions.BlogStackCustomException;
import com.blogstack.exceptions.BlogStackDataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestControllerAdvice
public class BlogStackUserManagementRestControllerAdvice {

    @ExceptionHandler(BlogStackCustomException.class)
    public ServiceResponseBean handleBlogStackCustomException(BlogStackCustomException blogStackCustomException) {
        return ServiceResponseBean.builder()
                .message(blogStackCustomException.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BlogStackDataNotFoundException.class)
    public ServiceResponseBean handleBlogStackUserManagementDataNotFoundException(BlogStackDataNotFoundException blogStackDataNotFoundException) {
        return ServiceResponseBean.builder()
                .message(blogStackDataNotFoundException.getMessage())
                .build();
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ServiceResponseBean handleBlogStackUserManagementFileNotFoundException(FileNotFoundException fileNotFoundException){
        return ServiceResponseBean.builder()
                .message(fileNotFoundException.getMessage())
                .build();
    }

    @ExceptionHandler(IOException.class)
    public ServiceResponseBean handleBlogStackUserManagementIOException(IOException ioException){
        return ServiceResponseBean.builder()
                .message(ioException.getMessage())
                .build();
    }

}