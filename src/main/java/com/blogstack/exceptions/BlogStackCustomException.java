package com.blogstack.exceptions;

public class BlogStackCustomException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    public BlogStackCustomException(String message) {
        super(message);
    }

    public BlogStackCustomException(String message, Throwable throwable) {
        super(message, throwable);
    }
}