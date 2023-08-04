package com.blogstack.service.impl;

import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.beans.response.ServiceResponseBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackRoleDetail;
import com.blogstack.entities.BlogStackUser;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.exceptions.BlogStackDataNotFoundException;
import com.blogstack.mappers.entity.pojo.IBlogStackUserEntityPojoMapper;
import com.blogstack.mappers.pojo.entity.IBlogStackUserPojoEntityMapper;
import com.blogstack.repository.IBlogStackRoleDetailRepository;
import com.blogstack.repository.IBlogStackUserRepository;
import com.blogstack.service.IBlogStackSuperAdminService;
import com.blogstack.utils.BlogStackCommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BlogStackSuperAdminService implements IBlogStackSuperAdminService {

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;
    @Autowired
    private IBlogStackUserPojoEntityMapper blogStackUserPojoEntityMapper;

    @Autowired
    private IBlogStackUserEntityPojoMapper blogStackUserEntityPojoMapper;

    @Autowired
    private IBlogStackRoleDetailRepository blogStackRoleDetailRepository;

    @Autowired
    private IBlogStackUserRepository blogStackUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public ResponseEntity<?> addAdmin(SignUpRequestBean adminSignUpRequestBean) {

        Optional<BlogStackUser> isAdminAlreadyPresent = this.blogStackUserRepository.findByBsuEmailId(adminSignUpRequestBean.getEmailId());

        if(isAdminAlreadyPresent.isPresent())
            throw new BlogStackDataNotFoundException("Admin with email "+adminSignUpRequestBean.getEmailId()+" already exists");
        adminSignUpRequestBean.setPassword(passwordEncoder.encode(adminSignUpRequestBean.getPassword()));
        Optional<BlogStackRoleDetail> blogStackAdminRoleDetails = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(BlogStackMessageConstants.ADMIN_ROLE);
        adminSignUpRequestBean.setBlogStackRoleDetails(blogStackAdminRoleDetails.stream()
                                                        .collect(Collectors.toSet()));
        String userId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.USER_ID.getValue());
        // BlogStackUser blogStackNewAdmin = this.blogStackUserRepository.saveAndFlush(this.blogStackUserPojoEntityMapper.INSTANCE.userPojoToUserEntity(adminSignUpRequestBean));
        BlogStackUser blogStackAdminUser = this.blogStackUserRepository.saveAndFlush(BlogStackUser.builder()
                .bsuEmailId(adminSignUpRequestBean.getEmailId())
                .blogStackRoleDetails(blogStackAdminRoleDetails.stream().collect(Collectors.toSet()))
                .bsuAddress(adminSignUpRequestBean.getAddress())
                .bsuCreatedBy(springApplicationName)
                .bsuCreatedDate(LocalDateTime.now())
                .bsuDateOfBirth(adminSignUpRequestBean.getDateOfBirth())
                .bsuFirstName(adminSignUpRequestBean.getFirstName())
                .bsuGender(adminSignUpRequestBean.getGender())
                .bsuLastName(adminSignUpRequestBean.getLastName())
                .bsuMiddleName(adminSignUpRequestBean.getMiddleName())
                .bsuPassword(adminSignUpRequestBean.getPassword())
                .bsuPhoneNumber(adminSignUpRequestBean.getPhoneNumber())
                .bsuUserId(userId)
                // .bsuStatus()
                .build());
        /*return ResponseEntity.ok().body(ServiceResponseBean.builder()
                                        .status(Boolean.TRUE)
                                        .data(this.blogStackUserEntityPojoMapper.INSTANCE.mapUserMasterEntityPojoMapping(blogStackAdminUser))
                                        .message(BlogStackMessageConstants.USER_CREATED_SUCCESSFULLY));*/
        return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ServiceResponseBean.builder().status(Boolean.TRUE)
                        .message(BlogStackMessageConstants.USER_CREATED_SUCCESSFULLY)
                        .data(IBlogStackUserEntityPojoMapper.INSTANCE.mapUserMasterEntityPojoMapping(blogStackAdminUser))
                        .build());

    }

    @Override
    public ResponseEntity<?> getAllAdmins() {

        Optional<BlogStackRoleDetail> blogStackRoleDetails = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(BlogStackMessageConstants.ADMIN_ROLE);
        if(blogStackRoleDetails.isEmpty())
            throw new BlogStackDataNotFoundException("There in no admin registered on the portal");

        // List<BlogStackUser> byBlogStackRoleDetails = this.blogStackUserRepository.findByBlogStackRoleDetails(blogStackRoleDetails.get());
        List<BlogStackUser> foundBlogStackAdmin = this.blogStackUserRepository.findBlogStackUserByBlogStackRoleDetailsBrdRoleId(blogStackRoleDetails.get().getBrdRoleId());

        if(foundBlogStackAdmin == null)
            log.info("Null value");
        // transfering the list object of entity into list objects of pojo

        log.info("Before returning the data from the service layer");
        return new ResponseEntity<>(foundBlogStackAdmin,HttpStatus.OK);
    }
}
