package com.sss.rms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sss")
public class SssRmsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SssRmsServiceApplication.class, args);
    }
}