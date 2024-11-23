package com.sss.test.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "test-topic", consumerGroup = "test-consumer-group")
public class RockerMQListener implements RocketMQListener<String> {
    /**
     *  泛型指定业务消息类型
     *  如果写成MessageExt就可以获取消息的全部信息
     *      发生异常就消费消息失败
     */

    @Override
    public void onMessage(String s) {
        log.info(s);
    }
}
