package com.blogstack.beans.request;

import com.blogstack.commons.BlogStackCommonConstants;
import com.blogstack.commons.BlogStackMessageConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class UserRequestBean {

    @JsonProperty(value = "user_id")
    private String userId;

    @NotEmpty(message = BlogStackMessageConstants.EMAIL_CANT_BLANK)
    @JsonProperty(value = "email_id")
    private String emailId;

    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonProperty(value = "middle_name")
    private String middleName;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "address")
    private String address;

    @JsonProperty(value = "gender")
    private String gender;

    @JsonProperty(value = "profile_photo")
    private String profilePhoto;

    @JsonProperty(value = "date_of_birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BlogStackCommonConstants.DOB_DATE_FORMAT)
    private LocalDate dateOfBirth;

    @JsonIgnore
    private String status;

    @JsonIgnore
    private String modifiedBy;
}