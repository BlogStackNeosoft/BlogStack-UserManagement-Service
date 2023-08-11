package com.blogstack.commons;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BlogStackMessageConstants {

    INSTANCE;

    public static final String DATA_NOT_FOUND = "data not found";
    public static final String DATA_DELETED = "data deleted";
    public static final String USER_NOT_PRESENT = "User not present";
    public static final String ROLE_ID_CANT_EMPTY = "Role id cant be empty";
    public static final String ROLE_ALREADY_EXIST = "Role already exist";
    public static final String USER_ID_CANT_EMPTY = "User id cannot be empty";
    public static final String USER_ID_CANT_BLANK = "User id cannot be blank";
    public static final String LAST_NAME_CANT_BLANK = "Last name cannot be blank";
    public static final String EMAIL_CANT_BLANK = "Email cannot be Blank";
    public static final String USER_DOES_NOT_EXIST = "User does not exist";
    public static final String USER_STATUS_CANT_EMPTY = "User status cannot be empty";
    public static final String INCORRECT_PASSWORD = "Incorrect password";
    public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password Changed Successfully";
    public static final String ROLE_NAME_CANT_BLANK = "Role name cannot be blank";
    public static final String PASSWORD_CANT_BLANK = "Password cannot be blank";
    public static final String EMAIL_ID_ALREADY_EXISTS = "Email id already Exists";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String USER_CREATED_SUCCESSFULLY = "User created successfully";
    public static final String OTP_CANT_BLANK = "OTP cannot be blank";
    public static final String OTP_SENT_SUCCESSFULLY = "OTP sent successfully";
    public static final String OTP_VERIFY_SUCCESS = "OTP verify success";
    public static final String OTP_VERIFICATION_FAILURE = "OTP verification failure";
    public static final String USER_DEFAULT_ROLE = "user";
    public static final String ADMIN_ROLE = "admin";
}