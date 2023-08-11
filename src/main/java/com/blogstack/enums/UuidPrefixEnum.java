package com.blogstack.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum UuidPrefixEnum {
    USER_ID("USER_ID_"),
    ROLE_ID("ROLE_ID_");
    @Getter
    private String value;

    public static List<String> getAllValues() {
        return List.of(UuidPrefixEnum.values()).stream().map(data -> data.value).collect(Collectors.toList());
    }
}