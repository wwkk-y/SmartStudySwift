package com.sss.common.config;

import com.sss.common.util.RocketMQSendUtil;
import org.springframework.context.annotation.Bean;

public class BaseRocketMQConfig {
    @Bean
    public RocketMQSendUtil rocketMQSendUtil(){
        return new RocketMQSendUtil();
    }
}
