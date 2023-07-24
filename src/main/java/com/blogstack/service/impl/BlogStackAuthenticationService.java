package com.blogstack.service.impl;

import com.blogstack.beans.request.SignInRequestBean;
import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.beans.response.JwtResponseBean;
import com.blogstack.beans.response.ServiceResponseBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackRoleDetail;
import com.blogstack.entities.BlogStackUser;
import com.blogstack.enums.RoleStatusEnum;
import com.blogstack.enums.UserStatusEnum;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.exceptions.BlogStackDataNotFoundException;
import com.blogstack.helper.JwtHelper;
import com.blogstack.mappers.entity.pojo.IBlogStackUserEntityPojoMapper;
import com.blogstack.mappers.pojo.entity.IBlogStackUserPojoEntityMapper;
import com.blogstack.repository.IBlogStackRoleDetailRepository;
import com.blogstack.repository.IBlogStackUserRepository;
import com.blogstack.service.IBlogStackAuthenticationService;
import com.blogstack.utils.BlogStackCommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private IBlogStackRoleDetailRepository blogStackRoleDetailRepository;

    @Override
    public Optional<?> signUp(SignUpRequestBean signUpRequestBean) throws IOException {
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailIdIgnoreCase(signUpRequestBean.getEmailId());
        LOGGER.info("BlogStackUserOptional :: {}", blogStackUserOptional);

        if (blogStackUserOptional.isPresent())
            return Optional.of(ServiceResponseBean.builder().status(Boolean.FALSE).message(BlogStackMessageConstants.EMAIL_ID_ALREADY_EXISTS).build());

        String userId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.USER_ID.getValue());
        LOGGER.info("UserId :: {}", userId);

        Set<BlogStackRoleDetail> blogStackRoleDetails = signUpRequestBean.getBlogStackRoleDetails().stream().map(blogStackRoleDetail -> {
            Optional<BlogStackRoleDetail> blogStackRoleDetailOptional = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(blogStackRoleDetail.getBrdRoleName());
            return blogStackRoleDetailOptional
                    .orElseGet(() -> BlogStackRoleDetail.builder()
                            .brdRoleId(BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.ROLE_ID.getValue()))
                            .brdRoleName(blogStackRoleDetail.getBrdRoleName())
                            .brdStatus(RoleStatusEnum.ACTIVE.getValue())
                            .build());
        }).collect(Collectors.toSet());


        signUpRequestBean.setUserId(userId);
        signUpRequestBean.setStatus(UserStatusEnum.INACTIVE.getValue());
        signUpRequestBean.setCreatedBy(springApplicationName);
        signUpRequestBean.setPassword(this.bCryptPasswordEncoder.encode(signUpRequestBean.getPassword()));
        signUpRequestBean.setBlogStackRoleDetails(blogStackRoleDetails);

        BlogStackUser blogStackUser = this.blogStackUserRepository.saveAndFlush(this.blogStackUserPojoEntityMapper.INSTANCE.userPojoToUserEntity(signUpRequestBean));

        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackUserEntityPojoMapper.INSTANCE.mapUserMasterEntityPojoMapping(blogStackUser)).build());
    }

    @Override
    public Optional<?> signIn(SignInRequestBean signInRequestBean) {
        String accessToken, refreshToken;
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailIdIgnoreCase(signInRequestBean.getEmailId());
        if (blogStackUserOptional.isEmpty()) {
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.USER_NOT_PRESENT);
        }
        if (bCryptPasswordEncoder.matches(signInRequestBean.getPassword(), blogStackUserOptional.get().getBsuPassword())) {
            blogStackUserOptional.get().setBsuStatus(UserStatusEnum.ACTIVE.getValue());
            accessToken = this.jwtHelper.generateToken(signInRequestBean.getEmailId());
            refreshToken = this.jwtHelper.generateRefreshToken(signInRequestBean.getEmailId());
        } else {
            return Optional.of(ServiceResponseBean.builder().status(Boolean.FALSE).message(BlogStackMessageConstants.INCORRECT_PASSWORD).build());
        }
        this.blogStackUserRepository.saveAndFlush(blogStackUserOptional.get());
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(JwtResponseBean.builder().userId(blogStackUserOptional.get().getBsuEmailId()).jwtToken(accessToken).refreshToken(refreshToken).build()).build());
    }

    @Override
    public Optional<?> refreshTokens(String token) {
        String email = jwtHelper.getSubject(token);
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailIdIgnoreCase(email);
        if (blogStackUserOptional.isEmpty())
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.USER_NOT_PRESENT);
        else if(blogStackUserOptional.isPresent() && jwtHelper.validateToken(token))
        {
            String accessToken = this.jwtHelper.generateToken(email);
            String refreshToken= this.jwtHelper.generateRefreshToken(email);
            return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(JwtResponseBean.builder().userId(blogStackUserOptional.get().getBsuEmailId()).jwtToken(accessToken).refreshToken(refreshToken).build()).build());
        }
        else
            return Optional.of(ServiceResponseBean.builder().status(Boolean.FALSE).message(BlogStackMessageConstants.INVALID_TOKEN).build());
    }


}
