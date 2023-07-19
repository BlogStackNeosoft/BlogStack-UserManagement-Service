package com.blogstack.service.impl;

import com.blogstack.beans.request.RoleRequestBean;
import com.blogstack.beans.response.PageResponseBean;
import com.blogstack.beans.response.ServiceResponseBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackRoleDetail;
import com.blogstack.enums.RoleStatusEnum;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.exceptions.BlogStackDataNotFoundException;
import com.blogstack.mappers.entity.pojo.IBlogStackRoleDetailEntityPojoMapper;
import com.blogstack.mappers.pojo.entity.IBlogStackRoleDetailPojoEntityMapper;
import com.blogstack.repository.IBlogStackRoleDetailRepository;
import com.blogstack.service.IBlogStackRoleDetailService;
import com.blogstack.utils.BlogStackCommonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlogStackRoleDetailService implements IBlogStackRoleDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogStackUserService.class);

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;

    @Autowired
    private IBlogStackRoleDetailRepository blogStackRoleDetailRepository;

    @Autowired
    private IBlogStackRoleDetailPojoEntityMapper blogStackRoleDetailPojoEntityMapper;

    @Override
    public Optional<?> addRole(RoleRequestBean roleRequestBean) {
        Optional<BlogStackRoleDetail> blogStackRoleDetailOptional = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(roleRequestBean.getRoleName());
        LOGGER.info("BlogStackRoleDetailOptional :: {}", blogStackRoleDetailOptional);

        if (blogStackRoleDetailOptional.isPresent())
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.ROLE_ALREADY_EXIST);

        String roleId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.ROLE_ID.getValue());
        LOGGER.info("roleId :: {}", roleId);

        roleRequestBean.setRoleId(roleId);
        roleRequestBean.setStatus(RoleStatusEnum.ACTIVE.getValue());
        roleRequestBean.setCreatedBy(this.springApplicationName);
        BlogStackRoleDetail blogStackRoleDetail = this.blogStackRoleDetailRepository.saveAndFlush(this.blogStackRoleDetailPojoEntityMapper.INSTANCE.rolePojoToRoleEntity(roleRequestBean));
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackRoleDetailEntityPojoMapper.mapRoleEntityPojoMapping.apply(blogStackRoleDetail)).build());
    }

    @Override
    public Optional<?> fetchRoleByRoleName(String roleName) {
        Optional<BlogStackRoleDetail> blogStackRoleDetailOptional = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(roleName);
        LOGGER.info("BlogStackRoleDetailOptional :: {}", blogStackRoleDetailOptional);

        if(blogStackRoleDetailOptional.isEmpty())
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.DATA_NOT_FOUND);

        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackRoleDetailEntityPojoMapper.mapRoleEntityPojoMapping.apply(blogStackRoleDetailOptional.get())).build());
    }

    @Override
    public Optional<?> fetchAllRole(Integer page, Integer size) {
        Page<BlogStackRoleDetail> blogStackRoleDetailPage = this.blogStackRoleDetailRepository.findAll(PageRequest.of(page, size));
        LOGGER.debug("BlogStackRoleDetailPage :: {}", blogStackRoleDetailPage);

        if(CollectionUtils.isEmpty(blogStackRoleDetailPage.toList()))
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.DATA_NOT_FOUND);

        return Optional.of(ServiceResponseBean.builder()
                .status(Boolean.TRUE).data(PageResponseBean.builder().payload(IBlogStackRoleDetailEntityPojoMapper.mapRoleEntityListtoPojoListMapping.apply(blogStackRoleDetailPage.toList()))
                        .numberOfElements(blogStackRoleDetailPage.getNumberOfElements())
                        .pageSize(blogStackRoleDetailPage.getSize())
                        .totalElements(blogStackRoleDetailPage.getTotalElements())
                        .totalPages(blogStackRoleDetailPage.getTotalPages())
                        .currentPage(blogStackRoleDetailPage.getNumber())
                        .build()).build());
    }

    @Override
    public Optional<?> deleteRole(String roleName) {
        Optional<BlogStackRoleDetail> blogStackRoleDetailOptional = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(roleName);
        LOGGER.info("BlogStackRoleDetailOptional :: {}", blogStackRoleDetailOptional);

        if(blogStackRoleDetailOptional.isEmpty())
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.DATA_NOT_FOUND);

        blogStackRoleDetailOptional.get().setBrdStatus(RoleStatusEnum.DELETED.getValue());
        this.blogStackRoleDetailRepository.saveAndFlush(blogStackRoleDetailOptional.get());
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).message(BlogStackMessageConstants.DATA_DELETED).build());
    }

    @Override
    public Optional<?> updateRole(RoleRequestBean roleRequestBean) {
        Optional<BlogStackRoleDetail> blogStackRoleDetailOptional = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(roleRequestBean.getRoleName());
        LOGGER.debug("BlogStackRoleDetailOptional :: {}", blogStackRoleDetailOptional);

        if (blogStackRoleDetailOptional.isEmpty())
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.DATA_NOT_FOUND);

        roleRequestBean.setModifiedBy(this.springApplicationName);
        BlogStackRoleDetail blogStackRoleDetail = this.blogStackRoleDetailPojoEntityMapper.INSTANCE.updateRole.apply(roleRequestBean, blogStackRoleDetailOptional.get());
        LOGGER.debug("BlogStackRoleDetail :: {}", blogStackRoleDetail);

        this.blogStackRoleDetailRepository.saveAndFlush(blogStackRoleDetail);
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackRoleDetailEntityPojoMapper.mapRoleEntityPojoMapping.apply(blogStackRoleDetail)).build());
    }
}
