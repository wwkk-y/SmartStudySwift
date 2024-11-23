package com.sss.common.util;

import com.sss.common.constant.RmsConst;
import com.sss.common.mapper.MQLogMapper;
import lombok.NonNull;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.UUID;

public class RocketMQSendUtil {
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private MQLogMapper mqLogMapper;

    /**
     * 异步发送消息
     * @param destination 目的地 topic:tag
     * @param message 发送的消息
     * @param sendCallback 异步发送消息回调
     */
    public void asyncSend(@NonNull String destination, @NonNull String message, SendCallback sendCallback){
        String bid = UUID.randomUUID().toString();
        rocketMQTemplate.asyncSend(
                destination,
                MessageBuilder.withPayload(message).setHeader(RocketMQHeaders.KEYS, bid).build(),
                new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 日志记录：处理消息丢失
                mqLogMapper.sendSuccess(sendResult.getMsgId(), bid, destination, message);

                if(sendCallback != null){
                    sendCallback.onSuccess(sendResult);
                }
            }

            @Override
            public void onException(Throwable throwable) {
                // 日志记录：处理消息丢失
                mqLogMapper.sendFailed(bid, destination, message, throwable.getMessage());

                if(sendCallback != null){
                    sendCallback.onException(throwable);
                }
            }
        });
    }
}
