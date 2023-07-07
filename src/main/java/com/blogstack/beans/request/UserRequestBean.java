package com.blogstack.beans.request;

import com.blogstack.annotations.UserStatus;
import com.blogstack.commons.BlogStackCommonConstants;
import com.blogstack.commons.LocaleMessageCodeConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class UserRequestBean {

    @NotNull(message = LocaleMessageCodeConstants.USER_ID_CANT_BLANK)
    @JsonProperty(value = "user_id")
    private String userId;

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

    @JsonProperty(value = "roles")
    private Set<String> roles;

    @JsonProperty(value = "date_of_birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BlogStackCommonConstants.DOB_DATE_FORMAT)
    private LocalDate dateOfBirth;

    @UserStatus
    @NotEmpty(message = LocaleMessageCodeConstants.USER_STATUS_CANNOT_BE_EMPTY_OR_BLANK)
    @JsonProperty(value = "status")
    private String status;

    @JsonIgnore
    private String modifiedBy;
}

