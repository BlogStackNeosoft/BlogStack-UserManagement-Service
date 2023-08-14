package com.blogstack.configs;

import com.blogstack.beans.redis.BlogStackForgotPasswordBean;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisDatabaseConfigs {
    @Value("${application.aws.hostname}")
    private String awsSeverHostName;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPort(6379);
        jedisConnectionFactory.setHostName(this.awsSeverHostName);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }
    @Bean
    public RedisTemplate<String, BlogStackForgotPasswordBean> redisTemplate(){
        RedisTemplate<String,BlogStackForgotPasswordBean> redistemplate = new RedisTemplate<>();
        redistemplate.setKeySerializer(new StringRedisSerializer());
        redistemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(getObjectMapper(), Object.class));
        redistemplate.setHashKeySerializer(new StringRedisSerializer());
        redistemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(getObjectMapper(), Object.class));
        redistemplate.setConnectionFactory(redisConnectionFactory());
        return redistemplate;
    }
    @Primary
    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
}
