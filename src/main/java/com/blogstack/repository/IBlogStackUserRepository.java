package com.blogstack.repository;

import com.blogstack.entities.BlogStackUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBlogStackUserRepository extends JpaRepository<BlogStackUser, Long>, JpaSpecificationExecutor<BlogStackUser> {

    Optional<BlogStackUser> findByBsuEmailIdIgnoreCase(String emailId);

    Optional<BlogStackUser> findByBsuEmailId(String emailId);

    Optional<BlogStackUser> findByBsuUserId(String userId);
}