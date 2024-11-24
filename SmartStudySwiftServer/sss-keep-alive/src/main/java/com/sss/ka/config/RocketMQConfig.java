package com.sss.ka.config;

import com.sss.common.config.BaseRocketMQConfig;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RocketMQAutoConfiguration.class)
public class RocketMQConfig extends BaseRocketMQConfig {

}