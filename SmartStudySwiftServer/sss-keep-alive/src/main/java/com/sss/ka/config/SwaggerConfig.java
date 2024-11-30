package com.sss.ka.config;

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
                .apiBasePackage("com.sss.ka.controller") // 接口扫描路径
                .title("智学速练(SmartStudySwift)后台-ka模块") // 标题
                .description("智学速练后台相关接口文档") //  描述
                .contactName("ka") //  关系名
                .version("1.0") // 版本
                .enableSecurity(true)
                .build();
    }

    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return generateBeanPostProcessor();
    }

}
