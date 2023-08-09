package com.blogstack.service.impl;

import com.blogstack.beans.redis.BlogStackForgotPasswordBean;
import com.blogstack.beans.request.SignInRequestBean;
import com.blogstack.beans.request.SignUpRequestBean;
import com.blogstack.beans.response.JwtResponseBean;
import com.blogstack.beans.response.ServiceResponseBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackRoleDetail;
import com.blogstack.entities.BlogStackUser;
import com.blogstack.enums.UserStatusEnum;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.exceptions.BlogStackDataNotFoundException;
import com.blogstack.feign.clients.IBlogStackEmailFeignService;
import com.blogstack.helper.JwtHelper;
import com.blogstack.mappers.entity.pojo.IBlogStackUserEntityPojoMapper;
import com.blogstack.mappers.pojo.entity.IBlogStackUserPojoEntityMapper;
import com.blogstack.repository.IBlogStackRedisOprationsService;
import com.blogstack.repository.IBlogStackRoleDetailRepository;
import com.blogstack.repository.IBlogStackUserRepository;
import com.blogstack.service.IBlogStackAuthenticationService;
import com.blogstack.utils.BlogStackCommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BlogStackAuthenticationService implements IBlogStackAuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;
    private JwtHelper jwtHelper;
    private IBlogStackUserRepository blogStackUserRepository;
    private IBlogStackUserPojoEntityMapper blogStackUserPojoEntityMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private IBlogStackRoleDetailRepository blogStackRoleDetailRepository;
    private IBlogStackRedisOprationsService redisOprationsService;
    private IBlogStackEmailFeignService blogStackEmailFeignService;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public BlogStackAuthenticationService(JwtHelper jwtHelper, IBlogStackUserRepository blogStackUserRepository, IBlogStackUserPojoEntityMapper blogStackUserPojoEntityMapper, BCryptPasswordEncoder bCryptPasswordEncoder, IBlogStackRoleDetailRepository blogStackRoleDetailRepository, IBlogStackRedisOprationsService redisOprationsService, IBlogStackEmailFeignService blogStackEmailFeignService, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.jwtHelper = jwtHelper;
        this.blogStackUserRepository = blogStackUserRepository;
        this.blogStackUserPojoEntityMapper = blogStackUserPojoEntityMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.blogStackRoleDetailRepository = blogStackRoleDetailRepository;
        this.redisOprationsService = redisOprationsService;
        this.blogStackEmailFeignService = blogStackEmailFeignService;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Override
    public ResponseEntity<?> signUp(SignUpRequestBean signUpRequestBean) throws IOException {
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailIdIgnoreCase(signUpRequestBean.getEmailId());
        LOGGER.info("BlogStackUserOptional :: {}", blogStackUserOptional);

        if (blogStackUserOptional.isPresent())
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ServiceResponseBean.builder().status(Boolean.FALSE).message(BlogStackMessageConstants.EMAIL_ID_ALREADY_EXISTS).build());

        String userId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.USER_ID.getValue());
        LOGGER.info("UserId :: {}", userId);

        /*Set<BlogStackRoleDetail> blogStackRoleDetails = signUpRequestBean.getBlogStackRoleDetails().stream().map(blogStackRoleDetail -> {
            Optional<BlogStackRoleDetail> blogStackRoleDetailOptional = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(blogStackRoleDetail.getBrdRoleName());
            return blogStackRoleDetailOptional
                    .orElseGet(() -> BlogStackRoleDetail.builder()
                            .brdRoleId(BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.ROLE_ID.getValue()))
                            .brdRoleName(blogStackRoleDetail.getBrdRoleName())
                            .brdStatus(RoleStatusEnum.ACTIVE.getValue())
                            .build());
        }).collect(Collectors.toSet());*/

        // ANY USER WHO SIGNS UP FROM UI WILL ALWAYS BE ASSIGNED A ROLE 'USER'

        Optional<BlogStackRoleDetail> blogStackRoleDetailsByRoleName = this.blogStackRoleDetailRepository.findByBrdRoleNameIgnoreCase(BlogStackMessageConstants.USER_DEFAULT_ROLE);
        log.info(String.format("Role: %s", blogStackRoleDetailsByRoleName.isPresent()));

        signUpRequestBean.setUserId(userId);
        signUpRequestBean.setStatus(UserStatusEnum.INACTIVE.getValue());
        signUpRequestBean.setCreatedBy(springApplicationName);
        signUpRequestBean.setPassword(this.bCryptPasswordEncoder.encode(signUpRequestBean.getPassword()));
        signUpRequestBean.setBlogStackRoleDetails(blogStackRoleDetailsByRoleName.stream()
                .collect(Collectors.toSet()));

        BlogStackUser blogStackUser = this.blogStackUserRepository.saveAndFlush(this.blogStackUserPojoEntityMapper.INSTANCE.userPojoToUserEntity(signUpRequestBean));
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            blogStackEmailFeignService.sendMessage(blogStackUser.getBsuEmailId(), blogStackUser.getBsuFirstName());
            LOGGER.info(Thread.currentThread().getName());
        }, threadPoolTaskExecutor);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ServiceResponseBean.builder().status(Boolean.TRUE).
                        message(BlogStackMessageConstants.USER_CREATED_SUCCESSFULLY)
                        .data(IBlogStackUserEntityPojoMapper.INSTANCE.mapUserMasterEntityPojoMapping(blogStackUser))
                        .build());
    }

    @Override
    public ResponseEntity<?> signIn(SignInRequestBean signInRequestBean) {
        String accessToken, refreshToken;
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailIdIgnoreCase(signInRequestBean.getEmailId());
        if (blogStackUserOptional.isEmpty())
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.USER_NOT_PRESENT);
        if (bCryptPasswordEncoder.matches(signInRequestBean.getPassword(), blogStackUserOptional.get().getBsuPassword())) {
            blogStackUserOptional.get().setBsuStatus(UserStatusEnum.ACTIVE.getValue());
            accessToken = this.jwtHelper.generateToken(signInRequestBean.getEmailId(), blogStackUserOptional.get().getBlogStackRoleDetails());
            refreshToken = this.jwtHelper.generateRefreshToken(signInRequestBean.getEmailId());
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(ServiceResponseBean.builder().status(Boolean.FALSE).message(BlogStackMessageConstants.INCORRECT_PASSWORD).build());
        }
        this.blogStackUserRepository.saveAndFlush(blogStackUserOptional.get());
        Optional<BlogStackUser> blogStackUser = this.blogStackUserRepository.findByBsuEmailIdIgnoreCase(signInRequestBean.getEmailId());
        Set<String> roleName = blogStackUser.get().getBlogStackRoleDetails().stream().map(BlogStackRoleDetail::getBrdRoleName).collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ServiceResponseBean.builder().status(Boolean.TRUE)
                        .data(JwtResponseBean.builder()
                                .userId(blogStackUserOptional.get().getBsuUserId())
                                .jwtToken(accessToken)
                                .refreshToken(refreshToken)
                                .blogStackRoleDetails(roleName)
                                .build())
                        .build());
    }

    @Override
    public ResponseEntity<?> refreshTokens(String token) {
        String email = jwtHelper.getSubject(token);
        Optional<BlogStackUser> blogStackUserOptional = this.blogStackUserRepository.findByBsuEmailIdIgnoreCase(email);
        if (blogStackUserOptional.isEmpty())
            throw new BlogStackDataNotFoundException(BlogStackMessageConstants.USER_NOT_PRESENT);
        else if (blogStackUserOptional.isPresent() && jwtHelper.validateToken(token)) {
            String accessToken = this.jwtHelper.generateToken(email, blogStackUserOptional.get().getBlogStackRoleDetails());
            return ResponseEntity.status(HttpStatus.OK).body(ServiceResponseBean.builder().status(Boolean.TRUE).data(JwtResponseBean.builder().userId(blogStackUserOptional.get().getBsuEmailId()).jwtToken(accessToken).refreshToken(token).build()).build());
        } else
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ServiceResponseBean.builder().status(Boolean.FALSE).message(BlogStackMessageConstants.INVALID_TOKEN).build());
    }

    @Override
    public ResponseEntity<?> forgotPasswordEmailGeneration(String blogStackUserEmail) {
        // firstCheckUserExistWith given email
        Optional<BlogStackUser> foundBlogStackUser = this.blogStackUserRepository.findByBsuEmailId(blogStackUserEmail);
        if (foundBlogStackUser.isEmpty())
            throw new BlogStackDataNotFoundException("The user does not exist in database");
        else {
            log.info("Before Sending data to redis");
            BlogStackForgotPasswordBean blogStackFogotPasswordBean = BlogStackForgotPasswordBean.builder()
                    .email(foundBlogStackUser.get().getBsuEmailId())
                    .otp(String.valueOf(new Random().nextInt(99999)))
                    .build();
            this.redisOprationsService.saveEmailAndOtp(blogStackFogotPasswordBean);
            log.info("otp: {}", blogStackFogotPasswordBean.getOtp());
            // send a mail with the generated otp
            CompletableFuture<Void> asyncEmailCall = CompletableFuture.runAsync(() -> {
                this.blogStackEmailFeignService.sendOTP(blogStackUserEmail, blogStackFogotPasswordBean.getOtp());
            }, this.threadPoolTaskExecutor);

            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> blogStackValidateOtp(BlogStackForgotPasswordBean blogStackForgotPasswordBean) {
        Optional<BlogStackForgotPasswordBean> foundObjectWithOtp = this.redisOprationsService.getOtpById(blogStackForgotPasswordBean.getEmail());
        if (foundObjectWithOtp.isPresent()) {
            if (foundObjectWithOtp.get().getOtp().equals(blogStackForgotPasswordBean.getOtp()))
                return new ResponseEntity<>(ServiceResponseBean.builder()
                        .status(Boolean.TRUE)
                        .message("user validated")
                        .build(), HttpStatus.OK);
        }

        throw new BlogStackDataNotFoundException("OTP of user not found");
    }
}