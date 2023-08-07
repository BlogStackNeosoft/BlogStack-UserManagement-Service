package com.blogstack.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@ToString
@Table(name = "blogstack_role_detail", schema = "user_management")
public class BlogStackRoleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brd_seq_id")
    @JsonIgnore
    private Long brdSeqId;

    @Column(name = "brd_role_id")
    @JsonIgnore
    private String brdRoleId;

    @Column(name = "brd_role_name")
    @JsonProperty(value = "role_name")
    private String brdRoleName;

    @ManyToMany(mappedBy = "blogStackRoleDetails",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<BlogStackUser> blogStackUsers;

    @Column(name = "brd_status")
    @JsonIgnore
    private String brdStatus;

}
