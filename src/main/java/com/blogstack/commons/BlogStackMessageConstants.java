package com.blogstack.commons;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BlogStackMessageConstants {

    INSTANCE;

    public static final String DATA_NOT_FOUND = "data not found";

    public static final String DATA_DELETED = "DATA_DELETED";

    public static final String USER_NOT_PRESENT = "User not present";

    public static final String ROLE_ID_CANT_EMPTY = "Role id cant empty";

    public static final String ROLE_ALREADY_EXIST = "Role already exist";

    public static final String USER_ID_CANT_EMPTY = "USER_ID_CANT_EMPTY";

    public static final String USER_ID_CANT_BLANK = "USER_ID_CANT_BLANK";

    public static final String EMAIL_CANT_BLANK = "Email cannot be b    lank";

    public static final String USER_STATUS_CANNOT_BE_EMPTY_OR_BLANK = "USER_STATUS_CANNOT_BE_EMPTY_OR_BLANK";

    public static final String INCORRECT_PASSWORD = "Incorrect password";

    public static final String ROLE_NAME_CANT_BLANK = "Role name cannot be blank";

    public static final String PASSWORD_CANT_BLANK = "Password cannot be blank";
    public static final String EMAIL_ID_ALREADY_EXISTS = "Email Id Already Exists";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String USER_CREATED_SUCCESSFULLY = "User created successfully";
}