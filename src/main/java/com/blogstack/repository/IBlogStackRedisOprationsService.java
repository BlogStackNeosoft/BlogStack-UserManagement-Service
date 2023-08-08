package com.blogstack.repository;

import com.blogstack.beans.redis.BlogStackForgotPasswordBean;

import java.util.Optional;


public interface IBlogStackRedisOprationsService {

    void saveEmailAndOtp(BlogStackForgotPasswordBean forgotPasswordBean);
    Optional<BlogStackForgotPasswordBean> getOtpById(String id);
}
