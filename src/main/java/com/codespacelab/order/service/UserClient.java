package com.codespacelab.order.service;

import com.codespacelab.order.config.OrderWebSecurityConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient("user")
@FeignClient("zuul")
@RibbonClient(name = "user")
public interface UserClient {

    @RequestMapping(method = RequestMethod.GET, value = "/user/user/validate")
    boolean isUserValid(@RequestParam(name = "id") Long id);
}
