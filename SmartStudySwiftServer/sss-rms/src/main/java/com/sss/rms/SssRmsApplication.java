package com.sss.rms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sss")
public class SssRmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SssRmsApplication.class, args);
    }
}