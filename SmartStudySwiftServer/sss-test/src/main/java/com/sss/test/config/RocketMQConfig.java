package com.sss.test.config;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.context.annotation.Import;

@Import(RocketMQAutoConfiguration.class)
public class RocketMQConfig {

}
