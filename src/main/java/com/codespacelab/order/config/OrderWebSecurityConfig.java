package com.codespacelab.order.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
//Don't need it for now
public class OrderWebSecurityConfig {

 //   @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {

        return new BasicAuthRequestInterceptor("admin", "admin");
    }
}
