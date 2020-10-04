package com.codespacelab.order.service;

import com.codespacelab.order.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedRate = 5000)
    public void displayMenu(){
        String menuItems = "The menu is Burger, Cookie, Soda, Chips";
      jmsTemplate.convertAndSend(JmsConfig.MENU_QUEUE,  menuItems );
    }
}
