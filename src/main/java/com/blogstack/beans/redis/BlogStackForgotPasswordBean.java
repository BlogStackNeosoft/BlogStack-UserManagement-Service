package com.blogstack.beans.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@RedisHash(value = "email")
public class BlogStackForgotPasswordBean implements Serializable {
    private String email;
    private String otp;
}
