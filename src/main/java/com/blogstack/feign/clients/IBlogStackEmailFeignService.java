package com.blogstack.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "email" , url = "http://localhost:9091/v1.0/email")
public interface IBlogStackEmailFeignService {

    @PostMapping()
    public void sendMessage(@RequestParam(value = "to")String to , @RequestParam(value = "firstName") String firstName );

    @PostMapping("/otp")
    public void sendOTP(@RequestParam(value = "to") String to,@RequestParam(value = "OTP")String otp);


    }
