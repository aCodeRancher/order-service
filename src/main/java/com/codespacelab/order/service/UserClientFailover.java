package com.codespacelab.order.service;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class UserClientFailover implements UserClient {
    public boolean isUserValid( Long id){
        return false;
    }
}
