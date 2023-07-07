package com.blogstack.exceptions;

public class BlogStackDataNotFoundException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    public BlogStackDataNotFoundException(String message) {
        super(message);
    }

    public BlogStackDataNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}