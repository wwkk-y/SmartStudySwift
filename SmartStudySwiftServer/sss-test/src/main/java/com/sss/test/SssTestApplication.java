package com.sss.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages="com.sss")
public class SssTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SssTestApplication.class, args);
    }
}