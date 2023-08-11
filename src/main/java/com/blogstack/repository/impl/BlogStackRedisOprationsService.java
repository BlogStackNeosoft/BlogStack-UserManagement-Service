//package com.blogstack.repository.impl;
//
//import com.blogstack.beans.redis.BlogStackForgotPasswordBean;
//import com.blogstack.repository.IBlogStackRedisOprationsService;
//import jakarta.annotation.Resource;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public class BlogStackRedisOprationsService implements IBlogStackRedisOprationsService {
//
//    private final String hashReferance = "BlogStackUser";
//
//    @Resource(name = "redisTemplate")
//    private HashOperations<String,String,BlogStackForgotPasswordBean> hashOperations;
//
//    @Override
//    public void saveEmailAndOtp(BlogStackForgotPasswordBean forgotPasswordBean) {
//        this.hashOperations.putIfAbsent(hashReferance, forgotPasswordBean.getEmail(),forgotPasswordBean);
//    }
//
//    @Override
//    public Optional<BlogStackForgotPasswordBean> getOtpById(String id) {
//        return Optional.of(this.hashOperations.get(hashReferance,id));
//    }
//}