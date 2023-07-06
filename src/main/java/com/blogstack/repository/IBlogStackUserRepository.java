package com.blogstack.repository;

import com.blogstack.entities.BlogStackUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBlogStackUserRepository extends JpaRepository<BlogStackUser, Long>, JpaSpecificationExecutor<BlogStackUser> {



}
