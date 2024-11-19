package com.sss.security.config;

import com.sss.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@EnableCaching // SpringCaching
@Configuration
public class SecurityRedisConfig extends BaseRedisConfig {
}
