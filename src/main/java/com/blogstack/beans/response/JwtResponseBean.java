package com.blogstack.beans.response;

import com.blogstack.entities.BlogStackRoleDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponseBean {

    @JsonProperty(value = "user_id")
    private String userId;

    @JsonProperty(value = "jwt_token")
    private String jwtToken;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @JsonProperty(value = "user_roles")
    private Set<String> blogStackRoleDetails;
}