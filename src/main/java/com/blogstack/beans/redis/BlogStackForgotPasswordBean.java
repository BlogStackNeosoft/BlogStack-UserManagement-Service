package com.blogstack.beans.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogStackForgotPasswordBean implements Serializable {
    @JsonProperty(value = "email")
    @NotBlank(message = "Email cannot be Blank")
    private String email;
    @JsonProperty(value = "otp")
    @NotBlank(message = "OTP cannot be null")
    private String otp;
}
