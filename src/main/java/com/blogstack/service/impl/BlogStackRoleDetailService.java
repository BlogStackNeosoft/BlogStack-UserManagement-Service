package com.blogstack.service.impl;

import com.blogstack.beans.request.RoleRequestBean;
import com.blogstack.beans.response.PageResponseBean;
import com.blogstack.beans.response.ServiceResponseBean;
import com.blogstack.commons.MessageCodeConstants;
import com.blogstack.entities.BlogStackRoleDetail;
import com.blogstack.entities.BlogStackUser;
import com.blogstack.enums.RoleStatusEnum;
import com.blogstack.enums.UserStatusEnum;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.exceptions.BlogStackDataNotFoundException;
import com.blogstack.mappers.entity.pojo.IBlogStackRoleDetailEntityPojoMapper;
import com.blogstack.mappers.entity.pojo.IBlogStackUserEntityPojoMapper;
import com.blogstack.mappers.pojo.entity.IBlogStackRoleDetailPojoEntityMapper;
import com.blogstack.repository.IBlogStackRoleDetailRepository;
import com.blogstack.service.IBlogStackRoleDetailService;
import com.blogstack.utils.BlogStackCommonUtils;
import com.blogstack.utils.BlogStackSpecificationUtils;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
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
    public Mono<?> addRole(RoleRequestBean roleRequestBean) {
        Optional<BlogStackRoleDetail> blogStackRoleDetailOptional = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(roleRequestBean.getRoleName());
        LOGGER.info("BlogStackRoleDetailOptional :: {}", blogStackRoleDetailOptional);

        if (blogStackRoleDetailOptional.isPresent())
            return Mono.just(ServiceResponseBean.builder().status(Boolean.FALSE).message(MessageCodeConstants.ROLE_ALREADY_EXIST).build());

        String roleId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.ROLE_ID.getValue());
        LOGGER.info("roleId :: {}", roleId);

        roleRequestBean.setRoleId(roleId);
        roleRequestBean.setStatus(RoleStatusEnum.ACTIVE.getValue());
        roleRequestBean.setCreatedBy(this.springApplicationName);
        BlogStackRoleDetail blogStackRoleDetail = this.blogStackRoleDetailRepository.saveAndFlush(this.blogStackRoleDetailPojoEntityMapper.INSTANCE.rolePojoToRoleEntity(roleRequestBean));
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackRoleDetailEntityPojoMapper.mapRoleEntityPojoMapping.apply(blogStackRoleDetail)).build());
    }

    @Override
    public Mono<?> fetchRoleByRoleId(String roleId) {
        Optional<BlogStackRoleDetail> blogStackRoleDetailOptional = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(roleId);
        LOGGER.info("BlogStackRoleDetailOptional :: {}", blogStackRoleDetailOptional);

        if(blogStackRoleDetailOptional.isEmpty())
            return Mono.error(new BlogStackDataNotFoundException(MessageCodeConstants.DATA_NOT_FOUND));

        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackRoleDetailEntityPojoMapper.mapRoleEntityPojoMapping.apply(blogStackRoleDetailOptional.get())).build());
    }

    @Override
    public Mono<?> fetchAllRole(String filterCriteria, String sortCriteria, Integer page, Integer size) {
        Specification<BlogStackRoleDetail> specification = null;
        if (StringUtils.isNotEmpty(filterCriteria)) {
            String entityFilterCriteria = BlogStackSpecificationUtils.INSTANCE.convertFilterCriteriaToEntityFilterCriteria(filterCriteria, "brd");
            LOGGER.debug("EntityFilterCriteria :: {}", entityFilterCriteria);
            specification = BlogStackSpecificationUtils.INSTANCE.buildSpecificaton(entityFilterCriteria, new ArrayList<>());
        }
        Sort sort = StringUtils.isNotEmpty(sortCriteria) ? BlogStackSpecificationUtils.INSTANCE.convertSortCriteriaToEntitySortCriteria(sortCriteria, "brd") : Sort.by("brdSeqId").ascending();

        assert specification != null;
        Page<BlogStackRoleDetail> blogStackRoleDetailPage = this.blogStackRoleDetailRepository.findAll(specification, PageRequest.of(page, size, sort));
        LOGGER.debug("BlogStackRoleDetailPage :: {}", blogStackRoleDetailPage);

        return CollectionUtils.isNotEmpty(blogStackRoleDetailPage.toList()) ? Mono.just(ServiceResponseBean.builder()
                .status(Boolean.TRUE).data(PageResponseBean.builder().payload(IBlogStackRoleDetailEntityPojoMapper.mapRoleEntityListtoPojoListMapping.apply(blogStackRoleDetailPage.toList()))
                        .numberOfElements(blogStackRoleDetailPage.getNumberOfElements())
                        .pageSize(blogStackRoleDetailPage.getSize())
                        .totalElements(blogStackRoleDetailPage.getTotalElements())
                        .totalPages(blogStackRoleDetailPage.getTotalPages())
                        .currentPage(blogStackRoleDetailPage.getNumber())
                        .build()).build())
                : Mono.error(new BlogStackDataNotFoundException(MessageCodeConstants.DATA_NOT_FOUND));
    }

    @Override
    public Mono<?> deleteRole(String roleName) {
        Optional<BlogStackRoleDetail> blogStackRoleDetailOptional = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(roleName);
        LOGGER.info("BlogStackRoleDetailOptional :: {}", blogStackRoleDetailOptional);

        if(blogStackRoleDetailOptional.isEmpty())
            return Mono.just(ServiceResponseBean.builder().status(Boolean.FALSE).message(MessageCodeConstants.DATA_NOT_FOUND).build());

        blogStackRoleDetailOptional.get().setBrdStatus(RoleStatusEnum.DELETED.getValue());
        blogStackRoleDetailOptional.get().setBrdModifiedBy(springApplicationName);
        blogStackRoleDetailOptional.get().setBrdModifiedDate(LocalDateTime.now());
        this.blogStackRoleDetailRepository.saveAndFlush(blogStackRoleDetailOptional.get());
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).message(MessageCodeConstants.DATA_DELETED).build());
    }

    @Override
    public Mono<?> updateRole(RoleRequestBean roleRequestBean) {
        Optional<BlogStackRoleDetail> blogStackRoleDetailOptional = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(roleRequestBean.getRoleName());
        LOGGER.debug("BlogStackRoleDetailOptional :: {}", blogStackRoleDetailOptional);

        if (blogStackRoleDetailOptional.isEmpty())
            return Mono.error(new BlogStackDataNotFoundException(MessageCodeConstants.DATA_NOT_FOUND));

        roleRequestBean.setModifiedBy(this.springApplicationName);
        BlogStackRoleDetail blogStackRoleDetail = this.blogStackRoleDetailPojoEntityMapper.INSTANCE.updateRole.apply(roleRequestBean, blogStackRoleDetailOptional.get());
        LOGGER.debug("BlogStackRoleDetail :: {}", blogStackRoleDetail);

        this.blogStackRoleDetailRepository.saveAndFlush(blogStackRoleDetail);
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackRoleDetailEntityPojoMapper.mapRoleEntityPojoMapping.apply(blogStackRoleDetail)).build());
    }
}
