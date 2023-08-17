package com.blogstack.feign.clients;

import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "BlogStackQNAHub", url = "http://localhost:9090/v1.0/question")
public interface IBlogStackQuestionFeignService {

    @GetMapping("/all-questions/{user_id}")
    ResponseEntity<?> fetchAllQuestionsByUserId(@PathVariable(value = "user_id") @NotBlank(message = "user id can not be blank") String userId);

}