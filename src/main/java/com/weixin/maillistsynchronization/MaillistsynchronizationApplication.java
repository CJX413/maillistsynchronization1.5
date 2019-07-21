package com.weixin.maillistsynchronization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MaillistsynchronizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaillistsynchronizationApplication.class, args);
    }

}
