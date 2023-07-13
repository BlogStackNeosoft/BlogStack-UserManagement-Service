package com.blogstack.beans.response;

import com.blogstack.commons.BlogStackCommonConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResponseBean {

    @JsonProperty(value = "role_id")
    private String roleId;

    @JsonProperty(value = "role_name")
    private String roleName;

    private String status;

    @JsonProperty(value = "added_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BlogStackCommonConstants.DATE_FORMAT)
    private LocalDateTime addedOn;

    @JsonProperty(value = "modified_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BlogStackCommonConstants.DATE_FORMAT)
    private LocalDateTime modifiedOn;

}