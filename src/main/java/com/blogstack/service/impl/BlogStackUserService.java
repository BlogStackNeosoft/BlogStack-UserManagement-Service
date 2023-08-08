package com.blogstack.service.impl;

import com.blogstack.beans.request.UserRequestBean;
import com.blogstack.beans.response.PageResponseBean;
import com.blogstack.beans.response.ServiceResponseBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackRoleDetail;
import com.blogstack.entities.BlogStackUser;
import com.blogstack.enums.UserStatusEnum;
import com.blogstack.exceptions.BlogStackDataNotFoundException;
import com.blogstack.mappers.entity.pojo.IBlogStackUserEntityPojoMapper;
import com.blogstack.mappers.pojo.entity.IBlogStackUserPojoEntityMapper;
import com.blogstack.repository.IBlogStackRoleDetailRepository;
import com.blogstack.repository.IBlogStackUserRepository;
import com.blogstack.service.IBlogStackUserService;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@CacheConfig(cacheNames = "blogstack-user-management")
@Transactional
public class BlogStackUserService implements IBlogStackUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogStackUserService.class);

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;

    @Autowired
    private IBlogStackUserRepository blogStackUserRepository;

    @Autowired
    private IBlogStackRoleDetailRepository blogStackRoleDetailRepository;

    @Autowired
    private IBlogStackUserPojoEntityMapper blogStackUserPojoEntityMapper;

    @Override
    public ResponseEntity<?> fetchAll(Integer page, Integer size) {
        Page<BlogStackUser> blogStackUserPage = this.blogStackUserRepository.findAll(PageRequest.of(page, size));
        LOGGER.debug("BlogStackUserPage :: {}", blogStackUserPage);

        if (CollectionUtils.isEmpty(blogStackUserPage.toList()))
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.DATA_NOT_FOUND);

        return ResponseEntity.status(HttpStatus.OK).body(ServiceResponseBean.builder()
                .status(Boolean.TRUE).data(PageResponseBean.builder().payload(IBlogStackUserEntityPojoMapper.mapUserMasterEntityListToPojoListMapping.apply(blogStackUserPage.toList()))
                        .numberOfElements(blogStackUserPage.getNumberOfElements())
                        .pageSize(blogStackUserPage.getSize())
                        .totalElements(blogStackUserPage.getTotalElements())
                        .totalPages(blogStackUserPage.getTotalPages())
                        .currentPage(blogStackUserPage.getNumber())
                        .build()).build());
    }

    @Override
    @Cacheable(key = "#emailId")
    public ResponseEntity<?> fetchUserById(String emailId) {
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailIdIgnoreCase(emailId);
        LOGGER.info("BlogStackUserOptional :: {}", blogStackUserOptional);

        if (blogStackUserOptional.isEmpty())
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.DATA_NOT_FOUND);

        Set<BlogStackRoleDetail> roleDetails = new HashSet<>();
        roleDetails.addAll(this.blogStackRoleDetailRepository.findBlogStackRoleDetailsByBlogStackUsersBsuUserId(blogStackUserOptional.get().getBsuUserId()));
        blogStackUserOptional.get().setBlogStackRoleDetails(roleDetails);

        return ResponseEntity.status(HttpStatus.OK).body(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackUserEntityPojoMapper.INSTANCE.mapUserMasterEntityPojoMapping(blogStackUserOptional.get())).build());
    }

    @Override
    @CachePut(key = "#userRequestBean", value = "blogstack-user-management")
    public ResponseEntity<?> updateUser(UserRequestBean userRequestBean) {
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailIdIgnoreCase(userRequestBean.getEmailId());
        LOGGER.info("BlogStackUserOptional :: {}", blogStackUserOptional);

        if (blogStackUserOptional.isEmpty())
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.DATA_NOT_FOUND);

        userRequestBean.setModifiedBy(this.springApplicationName);
        BlogStackUser blogStackUser = this.blogStackUserPojoEntityMapper.INSTANCE.updateUser.apply(userRequestBean, blogStackUserOptional.get());
        LOGGER.debug("BlogStackUser :: {}", blogStackUser);

        this.blogStackUserRepository.saveAndFlush(blogStackUser);
        return ResponseEntity.status(HttpStatus.OK).body(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackUserEntityPojoMapper.INSTANCE.mapUserMasterEntityPojoMapping(blogStackUser)).build());
    }

    @Override
    public ResponseEntity<?> deleteUser(String emailId) {
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailIdIgnoreCase(emailId);
        LOGGER.info("BlogStackUserOptional :: {}", blogStackUserOptional);

        if (blogStackUserOptional.isEmpty())
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.DATA_NOT_FOUND);

        blogStackUserOptional.get().setBsuStatus(UserStatusEnum.DELETE.getValue());
        blogStackUserOptional.get().setBsuModifiedBy(springApplicationName);
        this.blogStackUserRepository.saveAndFlush(blogStackUserOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(ServiceResponseBean.builder().status(Boolean.TRUE).message(BlogStackMessageConstants.DATA_DELETED).build());
    }
}