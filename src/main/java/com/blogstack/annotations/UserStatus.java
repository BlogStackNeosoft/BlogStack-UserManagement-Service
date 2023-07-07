package com.blogstack.annotations;

import com.blogstack.annotations.constraints.UserStatusValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, PARAMETER, FIELD })
@Constraint(validatedBy = UserStatusValidation.class)
public @interface UserStatus {

    String message() default "";

    Class<?>[] groups() default {};


    Class<? extends Payload>[] payload() default {};

    boolean mandatory() default true;

}
