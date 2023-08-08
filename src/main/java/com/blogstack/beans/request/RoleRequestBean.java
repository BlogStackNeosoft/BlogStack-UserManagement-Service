package com.blogstack.beans.request;

import com.blogstack.commons.BlogStackMessageConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleRequestBean {

    @JsonProperty(value = "role_id")
    private String roleId;

    @NotBlank(message = BlogStackMessageConstants.ROLE_NAME_CANT_BLANK)
    @JsonProperty(value = "role_name")
    private String roleName;

    private  String status;

    @JsonIgnore
    private String createdBy;

    @JsonIgnore
    private String modifiedBy;

}
