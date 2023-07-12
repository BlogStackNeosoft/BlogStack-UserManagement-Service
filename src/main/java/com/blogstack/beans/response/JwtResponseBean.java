package com.blogstack.beans.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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

}
