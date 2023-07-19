package com.blogstack.repository;

import com.blogstack.entities.BlogStackUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBlogStackUserRepository extends JpaRepository<BlogStackUser, Long>{

    Optional<BlogStackUser> findByBsuEmailIdIgnoreCase(String emailId);

    Optional<BlogStackUser> findByBsuEmailId(String emailId);


}