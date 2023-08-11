package com.blogstack.beans.request;

import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackRoleDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpRequestBean {

    @JsonIgnore
    private String userId;

    @JsonProperty(value = "email_id")
    @NotBlank(message = BlogStackMessageConstants.EMAIL_CANT_BLANK)
    private String emailId;

    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonProperty(value = "middle_name")
    private String middleName;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "gender")
    private String gender;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "password")
    @NotBlank(message = BlogStackMessageConstants.PASSWORD_CANT_BLANK)
    private String password;

    @JsonProperty(value = "blogStackRoleDetails")
    @NotNull(message = BlogStackMessageConstants.ROLE_NAME_CANT_BLANK)
    private Set<BlogStackRoleDetail> blogStackRoleDetails;

    @JsonIgnore
    private String status;

    @JsonProperty(value = "date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty(value = "address")
    private String address;

    @JsonProperty(value = "profile_photo")
    private String profilePhoto;

    @JsonIgnore
    private String createdBy;

    @JsonIgnore
    private String modifiedBy;
}