package com.blogstack.service.impl;

import com.blogstack.beans.request.SignInRequestBean;
import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.beans.response.ServiceResponseBean;
import com.blogstack.beans.response.JwtResponseBean;
import com.blogstack.commons.LocaleMessageCodeConstants;
import com.blogstack.entities.BlogStackUser;
import com.blogstack.enums.UserStatusEnum;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.mappers.entity.pojo.IBlogStackUserEntityPojoMapper;
import com.blogstack.mappers.pojo.entity.IBlogStackUserPojoEntityMapper;
import com.blogstack.repository.IBlogStackUserRepository;
import com.blogstack.helper.JwtHelper;
import com.blogstack.service.IBlogStackAuthenticationService;
import com.blogstack.utils.BlogStackCommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Service
public class BlogStackAuthenticationService implements IBlogStackAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Autowired
    private JwtHelper jwtHelper;

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;

    @Autowired
    private IBlogStackUserRepository blogStackUserRepository;

    @Autowired
    private IBlogStackUserPojoEntityMapper blogStackUserPojoEntityMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Mono<?> signUp(SignUpRequestBean signUpRequestBean) {
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailIdIgnoreCase(signUpRequestBean.getEmailId());
        LOGGER.info("BlogStackUserOptional :: {}", blogStackUserOptional);

        if (blogStackUserOptional.isPresent())
            return Mono.just(ServiceResponseBean.builder().status(Boolean.FALSE).message("Email Id Already Exist try with another Email Id.").build());

        String userId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.USER_ID.getValue());
        LOGGER.info("UserId :: {}", userId);

        signUpRequestBean.setUserId(userId);
        signUpRequestBean.setStatus(UserStatusEnum.INACTIVE.getValue());
        signUpRequestBean.setCreatedBy(springApplicationName);
        signUpRequestBean.setPassword(this.bCryptPasswordEncoder.encode(signUpRequestBean.getPassword()));
        BlogStackUser blogStackUser = this.blogStackUserRepository.saveAndFlush(this.blogStackUserPojoEntityMapper.INSTANCE.userPojoToUserEntity(signUpRequestBean));
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackUserEntityPojoMapper.INSTANCE.mapUserMasterEntityPojoMapping(blogStackUser)).build());
    }

    @Override
    public Mono<?> signIn(SignInRequestBean signInRequestBean) {
        String token;
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailIdIgnoreCase(signInRequestBean.getEmailId());
        if (blogStackUserOptional.isEmpty()) {
            return Mono.just(ServiceResponseBean.builder().status(Boolean.FALSE).message(LocaleMessageCodeConstants.USER_NOT_PRESENT).build());
        }
        if (bCryptPasswordEncoder.matches(signInRequestBean.getPassword(), blogStackUserOptional.get().getBsuPassword())) {
            blogStackUserOptional.get().setBsuStatus(UserStatusEnum.ACTIVE.getValue());
            token = this.jwtHelper.generateToken(signInRequestBean.getEmailId());
        } else {
            return Mono.just(ServiceResponseBean.builder().status(Boolean.FALSE).message(LocaleMessageCodeConstants.INCORRECT_PASSWORD).build());
        }
        this.blogStackUserRepository.saveAndFlush(blogStackUserOptional.get());
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(JwtResponseBean.builder().userId(blogStackUserOptional.get().getBsuEmailId()).jwtToken(token).build()).build());
    }
}
