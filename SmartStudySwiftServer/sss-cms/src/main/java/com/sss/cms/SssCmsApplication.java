package com.sss.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sss")
public class SssCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SssCmsApplication.class, args);
    }
}