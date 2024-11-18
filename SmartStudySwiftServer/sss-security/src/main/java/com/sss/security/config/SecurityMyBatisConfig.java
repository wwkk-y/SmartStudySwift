package com.sss.security.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.sss.security.mapper")
public class SecurityMyBatisConfig {
}