package com.sss.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sss")
public class SssLmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SssLmsApplication.class, args);
    }
}