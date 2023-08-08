package com.blogstack.configs;

import com.blogstack.beans.redis.BlogStackForgotPasswordBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisDatabaseConfigs {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("ec2-3-110-224-124.ap-south-1.compute.amazonaws.com");
        jedisConnectionFactory.setPort(6379);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, BlogStackForgotPasswordBean> redisTemplate(){
        RedisTemplate<String,BlogStackForgotPasswordBean> redistemplate = new RedisTemplate<>();
        redistemplate.setConnectionFactory(redisConnectionFactory());
        return redistemplate;
    }
}
