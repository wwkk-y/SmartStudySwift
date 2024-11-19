package com.sss.ums;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.sss")
public class SssUmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SssUmsApplication.class, args);
    }
}