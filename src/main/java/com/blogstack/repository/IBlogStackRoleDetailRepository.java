package com.blogstack.repository;

import com.blogstack.entities.BlogStackRoleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBlogStackRoleDetailRepository extends JpaRepository<BlogStackRoleDetail, Long>, JpaSpecificationExecutor<BlogStackRoleDetail> {

    public Optional<BlogStackRoleDetail> findByBrdRoleNameIgnoreCase(String roleName);

}
