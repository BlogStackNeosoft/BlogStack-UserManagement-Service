package com.blogstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BlogStackUserManagementService {

    public static void main(String[] args) {
        SpringApplication.run(BlogStackUserManagementService.class, args);
    }

}
