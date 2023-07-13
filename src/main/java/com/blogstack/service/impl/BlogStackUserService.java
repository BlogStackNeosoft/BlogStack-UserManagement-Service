package com.blogstack.service.impl;

import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.beans.request.UserRequestBean;
import com.blogstack.beans.response.PageResponseBean;
import com.blogstack.beans.response.ServiceResponseBean;
import com.blogstack.commons.MessageCodeConstants;
import com.blogstack.entities.BlogStackRoleDetail;
import com.blogstack.entities.BlogStackUser;
import com.blogstack.enums.UserStatusEnum;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.exceptions.BlogStackDataNotFoundException;
import com.blogstack.mappers.entity.pojo.IBlogStackUserEntityPojoMapper;
import com.blogstack.mappers.pojo.entity.IBlogStackUserPojoEntityMapper;
import com.blogstack.repository.IBlogStackUserRepository;
import com.blogstack.service.IBlogStackUserService;
import com.blogstack.utils.BlogStackCommonUtils;
import com.blogstack.utils.BlogStackSpecificationUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class BlogStackUserService implements IBlogStackUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogStackUserService.class);

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;

    @Autowired
    private IBlogStackUserRepository blogStackUserRepository;

    @Autowired
    private IBlogStackUserPojoEntityMapper blogStackUserPojoEntityMapper;


    @Override
    public Mono<?> fetchAll(String filterCriteria, String sortCriteria, Integer page, Integer size) {
        Specification<BlogStackUser> specification = null;
        if (StringUtils.isNotEmpty(filterCriteria)) {
            String entityFilterCriteria = BlogStackSpecificationUtils.INSTANCE.convertFilterCriteriaToEntityFilterCriteria(filterCriteria, "bsu");
            LOGGER.debug("EntityFilterCriteria :: {}", entityFilterCriteria);
            specification = BlogStackSpecificationUtils.INSTANCE.buildSpecificaton(entityFilterCriteria, new ArrayList<>());
        }
        Sort sort = StringUtils.isNotEmpty(sortCriteria) ? BlogStackSpecificationUtils.INSTANCE.convertSortCriteriaToEntitySortCriteria(sortCriteria, "bsu") : Sort.by("bsuSeqId").ascending();

        Page<BlogStackUser> blogStackUserPage = this.blogStackUserRepository.findAll(specification, PageRequest.of(page, size, sort));
        LOGGER.debug("BlogStackUserPage :: {}", blogStackUserPage);

        return CollectionUtils.isNotEmpty(blogStackUserPage.toList()) ? Mono.just(ServiceResponseBean.builder()
                .status(Boolean.TRUE).data(PageResponseBean.builder().payload(IBlogStackUserEntityPojoMapper.mapUserMasterEntityListToPojoListMapping.apply(blogStackUserPage.toList()))
                        .numberOfElements(blogStackUserPage.getNumberOfElements())
                        .pageSize(blogStackUserPage.getSize())
                        .totalElements(blogStackUserPage.getTotalElements())
                        .totalPages(blogStackUserPage.getTotalPages())
                        .currentPage(blogStackUserPage.getNumber())
                        .build()).build())
                : Mono.error(new BlogStackDataNotFoundException(MessageCodeConstants.DATA_NOT_FOUND));
    }

    @Override
    public Mono<?> fetchUserById(String emailId) {
//        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuUserId(emailId);
//        LOGGER.info("BlogStackUserOptional :: {}", blogStackUserOptional);
//
//        if(blogStackUserOptional.isEmpty())
//            return Mono.error(new BlogStackDataNotFoundException(MessageCodeConstants.DATA_NOT_FOUND));
//        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackUserEntityPojoMapper.INSTANCE.mapUserMasterEntityPojoMapping(blogStackUserOptional.get())).build());
        Optional<BlogStackUser> byId = this.blogStackUserRepository.findById(BigInteger.ONE.longValue());
        LOGGER.info("Empty :: {}", byId.get().getBlogStackRoleDetails().isEmpty());
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(byId).build());
    }

    @Override
    public Mono<?> updateUser(UserRequestBean userRequestBean) {
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuUserId(userRequestBean.getUserId());
        LOGGER.debug("BlogStackUserOptional :: {}", blogStackUserOptional);

        if (blogStackUserOptional.isEmpty())
            return Mono.error(new BlogStackDataNotFoundException(MessageCodeConstants.DATA_NOT_FOUND));

        userRequestBean.setModifiedBy(this.springApplicationName);
        BlogStackUser blogStackUser = this.blogStackUserPojoEntityMapper.INSTANCE.updateUser.apply(userRequestBean, blogStackUserOptional.get());
        LOGGER.debug("BlogStackUser :: {}", blogStackUser);

        this.blogStackUserRepository.saveAndFlush(blogStackUser);
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackUserEntityPojoMapper.INSTANCE.mapUserMasterEntityPojoMapping(blogStackUser)).build());
    }

    @Override
    public Mono<?> deleteUser(String emailId) {
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailId(emailId);
        LOGGER.info("BlogStackUserOptional :: {}", blogStackUserOptional);

        if(blogStackUserOptional.isEmpty())
            return Mono.just(ServiceResponseBean.builder().status(Boolean.FALSE).message("User with this Email Id not found.").build());

        blogStackUserOptional.get().setBsuStatus(UserStatusEnum.DELETE.getValue());
        blogStackUserOptional.get().setBsuModifiedBy(springApplicationName);
        this.blogStackUserRepository.saveAndFlush(blogStackUserOptional.get());
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).message("User Deleted").build());
    }
}