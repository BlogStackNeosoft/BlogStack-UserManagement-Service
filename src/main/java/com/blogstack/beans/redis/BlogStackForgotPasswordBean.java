package com.blogstack.beans.redis;

import com.blogstack.commons.BlogStackMessageConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.ContentType;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Data
@Getter
@ToString
@Builder
@RedisHash(value = "email")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogStackForgotPasswordBean implements Serializable {
    private static final Long serialVersionUID = 1L;
    @JsonProperty(value = "email")
    @NotBlank(message = BlogStackMessageConstants.EMAIL_CANT_BLANK)
    private String email;
    @JsonProperty(value = "otp")
    @NotBlank(message = BlogStackMessageConstants.OTP_CANT_BLANK)
    private String otp;
}