package com.sss.ka;

import com.sss.ka.service.WebSocketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.sss")
public class SssKaApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SssKaApplication.class, args);
        WebSocketService.applicationContext = applicationContext;
    }
}