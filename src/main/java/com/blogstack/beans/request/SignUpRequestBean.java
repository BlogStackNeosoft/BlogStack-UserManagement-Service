package com.blogstack.beans.request;

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

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpRequestBean {

    @JsonProperty(value = "user_id")
    private String userId;

    @JsonProperty(value = "email_id")
    @NotBlank(message = "Email connot be blank")
    private String emailId;

    @JsonProperty(value = "last_name")
    @NotBlank(message = "Email connot be blank")
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
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @JsonProperty(value = "user_roles")
    @NotBlank(message = "Role can not be blank")
    private Set<BlogStackRoleDetail> blogStackRoleDetails;

    @JsonProperty(value = "status")
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
