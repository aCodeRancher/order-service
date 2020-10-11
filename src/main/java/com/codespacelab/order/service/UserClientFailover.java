package com.codespacelab.order.service;

import org.springframework.stereotype.Component;

@Component
public class UserClientFailover implements UserClient {
    public boolean isUserValid( Long id){
        return false;
    }
}
