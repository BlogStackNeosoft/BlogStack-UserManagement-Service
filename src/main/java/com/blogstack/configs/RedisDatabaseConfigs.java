package com.blogstack.configs;

import com.blogstack.beans.redis.BlogStackForgotPasswordBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisDatabaseConfigs {

    @Value("${application.aws.hostname}")
    private String awsSeverHostName;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPort(6379);
        jedisConnectionFactory.setHostName(this.awsSeverHostName);
        jedisConnectionFactory.setTimeout(10000 * 60 * 5);
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
