package com.blogstack.beans.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpRequestBean {

    @JsonProperty(value = "user_id")
    private String userId;

    @JsonProperty(value = "email_id")
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
    private String password;

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
