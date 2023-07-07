package com.blogstack.annotations.constraints;

import com.blogstack.annotations.UserStatus;
import com.blogstack.commons.BlogStackCommonConstants;
import com.blogstack.commons.LocaleMessageCodeConstants;
import com.blogstack.enums.UserStatusEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

public class UserStatusValidation implements ConstraintValidator<UserStatus, Object> {

    private boolean mandatory;

    @Override
    public void initialize(UserStatus userStatus) {
        this.mandatory = userStatus.mandatory();
    }

    @Override
    public boolean isValid(Object userStatus, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(LocaleMessageCodeConstants.USER_STATUS_VALIDATION
                .concat(BlogStackCommonConstants.INSTANCE.SPACE_STRING)
                .concat(BlogStackCommonConstants.INSTANCE.OPEN_SQUARE_BRACKET_STRING)
                .concat(UserStatusEnum.getAllValues().parallelStream().collect(Collectors.joining(BlogStackCommonConstants.INSTANCE.COMMA_STRING)).concat(BlogStackCommonConstants.INSTANCE.CLOSE_SQUARE_BRACKET_STRING))).addConstraintViolation();

        if (this.mandatory)
            return validationCondition(userStatus);

        if (userStatus != null)
            return validationCondition(userStatus);
        return Boolean.TRUE;
    }

    @SuppressWarnings("rawtypes")
    private Boolean validationCondition(Object userStatus) {
        return (userStatus instanceof Set userStatusSet) ? UserStatusEnum.getAllValues().stream().filter((userStatusSet)::contains).collect(Collectors.toSet()).size() > BigInteger.ZERO.intValue() : UserStatusEnum.getAllValues().stream().anyMatch(data -> data.equalsIgnoreCase((String) userStatus)) ? Boolean.TRUE : Boolean.FALSE;
    }

}
