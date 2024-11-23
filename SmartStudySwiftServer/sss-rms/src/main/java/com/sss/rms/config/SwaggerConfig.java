package com.sss.rms.config;

import com.sss.common.config.BaseSwaggerConfig;
import com.sss.common.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger相关配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.sss.rms.controller") // 接口扫描路径
                .title("SmartStudySwift后台") // 标题
                .description("SmartStudySwift后台-rms后台相关接口文档") // 描述
                .contactName("rms") // 关系名
                .version("1.0") // 版本
                .enableSecurity(true)
                .build();
    }

    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return generateBeanPostProcessor();
    }

}
