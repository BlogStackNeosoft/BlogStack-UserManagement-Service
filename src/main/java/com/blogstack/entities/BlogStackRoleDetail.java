package com.blogstack.entities;

import com.blogstack.controllers.BlogStackRoleController;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blogstack_role_detail", schema = "iam_management")
public class BlogStackRoleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brd_seq_id")
    private Long brdSeqId;

    @Column(name = "brd_role_id")
    private String brdRoleId;

    @Column(name = "brd_role_name")
    private String brdRoleName;

    @ManyToMany(mappedBy = "blogStackRoleDetails", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<BlogStackUser> blogStackUsers;

    @Column(name = "brd_status")
    private String brdStatus;

    @CreatedBy
    @Column(name = "brd_created_by")
    private String brdCreatedBy;

    @CreatedDate
    @Column(name = "brd_created_date")
    private LocalDateTime brdCreatedDate;

    @LastModifiedBy
    @Column(name = "brd_modified_by")
    private String brdModifiedBy;

    @LastModifiedDate
    @Column(name = "brd_modified_date")
    private LocalDateTime brdModifiedDate;
    
}
