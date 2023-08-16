package com.blogstack.beans.response;

import com.blogstack.commons.BlogStackCommonConstants;
import com.blogstack.entities.BlogStackRoleDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseBean {

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

    @JsonProperty(value = "bsuJwtSecret")
    private String bsuJwtSecret;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "user_roles")
    private Set<BlogStackRoleDetail> blogStackRoleDetails;

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty(value = "address")
    private String address;

    @JsonProperty(value = "profile_photo")
    private String profilePhoto;

    @JsonProperty(value="added_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BlogStackCommonConstants.DATE_FORMAT)
    private LocalDateTime addedOn;


    @JsonProperty(value = "modified_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BlogStackCommonConstants.DATE_FORMAT)
    private LocalDateTime modifiedOn;

}
