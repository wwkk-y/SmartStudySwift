package com.sss.qms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sss")
public class SssQmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SssQmsApplication.class, args);
    }
}
