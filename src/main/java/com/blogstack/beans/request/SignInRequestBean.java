package com.blogstack.beans.request;

import com.blogstack.commons.MessageCodeConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInRequestBean {

    @NotEmpty(message = MessageCodeConstants.EMAIL_CANT_BLANK)
    @JsonProperty(value = "email_id")
    private String emailId;

    @NotEmpty(message = MessageCodeConstants.PASSWORD_CANT_BLANK)
    private String password;
}