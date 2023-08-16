package com.blogstack.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class BlogStackSecurityUtils {

    public String randomStringGenerator(){
        int leftLimit = 97;  // character a
        int rightLimit = 122;  // characer z
        int targetStringLength = 24;

        Random random = new Random();

        return random.ints(leftLimit,rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new,StringBuilder::appendCodePoint,StringBuilder::append)
                .toString();
    }
}
