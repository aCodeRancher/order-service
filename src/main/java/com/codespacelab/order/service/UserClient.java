package com.codespacelab.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="user" , fallback = UserClientFailover.class)
public interface UserClient {

    @RequestMapping(method = RequestMethod.GET, value = "/user/validate")
    boolean isUserValid(@RequestParam(name = "id") Long id);
}
