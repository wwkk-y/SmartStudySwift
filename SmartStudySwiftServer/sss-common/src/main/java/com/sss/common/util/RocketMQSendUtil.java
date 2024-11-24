package com.sss.common.util;

import com.sss.common.constant.RmsConst;
import com.sss.common.mapper.MQLogMapper;
import lombok.NonNull;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.UUID;

public class RocketMQSendUtil {
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private MQLogMapper mqLogMapper;

    /**
     * 异步发送消息 自动设置业务key
     * @param destination 目的地 topic:tag
     * @param message 发送的消息
     */
    public void asyncSend(@NonNull String destination, @NonNull String message){
        asyncSend(destination, MessageBuilder.withPayload(message), null);
    }

    /**
     * 异步发送消息 自动设置业务key
     * @param destination 目的地 topic:tag
     * @param message 发送的消息
     * @param sendCallback 异步发送消息回调
     */
    public void asyncSend(@NonNull String destination, @NonNull String message, SendCallback sendCallback){
        asyncSend(destination, MessageBuilder.withPayload(message), sendCallback);
    }

    public void asyncSend(
            @NonNull String destination,
            @NonNull MessageBuilder<String> messageBuilder
    ){
        asyncSend(destination, messageBuilder, null);
    }

    /**
     * 异步发送消息 自动设置业务key
     * @param destination 目的地 topic:tag
     * @param messageBuilder 发送的消息
     * @param sendCallback 异步发送消息回调
     */
    public void asyncSend(
            @NonNull String destination,
            @NonNull MessageBuilder<String> messageBuilder,
            SendCallback sendCallback
    ){
        String bid = UUID.randomUUID().toString();
        Message<String> build = messageBuilder.setHeader(RocketMQHeaders.KEYS, bid).build();
        rocketMQTemplate.asyncSend(
                destination,
                build,
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        // 日志记录：处理消息丢失 -> FIXME 有问题 可能在消费之后才插入
                        mqLogMapper.sendSuccess(sendResult.getMsgId(), bid, destination, build.getPayload());

                        if(sendCallback != null){
                            sendCallback.onSuccess(sendResult);
                        }
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        // 日志记录：处理消息丢失
                        mqLogMapper.sendFailed(bid, destination, build.getPayload(), throwable.getMessage());

                        if(sendCallback != null){
                            sendCallback.onException(throwable);
                        }
                    }
                });
    }
}
