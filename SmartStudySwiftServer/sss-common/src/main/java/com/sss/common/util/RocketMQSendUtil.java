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

    /**
     * 日志代理回调类
     */
    private class SendCallBackLogImpl implements SendCallback{
        private final String bid;
        private final String destination;
        private final String message;
        private final SendCallback sendCallback;

        SendCallBackLogImpl(@NonNull String bid, @NonNull String destination, @NonNull String message, SendCallback sendCallback){
            this.bid = bid;
            this.destination = destination;
            this.message = message;
            this.sendCallback = sendCallback;
        }

        @Override
        public void onSuccess(SendResult sendResult) {
            // 日志记录：处理消息丢失 -> 有问题 可能在消费之后才插入 -> 在消费者逻辑使用自旋锁获取日志行
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
    }

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private MQLogMapper mqLogMapper;

    /**
     * 给消息设置业务id
     */
    private static String setAndGetBid(@NonNull MessageBuilder<String> messageBuilder){
        String bid = UUID.randomUUID().toString();
        messageBuilder.setHeader(RocketMQHeaders.KEYS, bid);
        return bid;
    }

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
        String bid = setAndGetBid(messageBuilder);
        Message<String> build = messageBuilder.build();
        rocketMQTemplate.asyncSend(
                destination,
                build,
                new SendCallBackLogImpl(bid, destination, build.getPayload(), sendCallback)
        );
    }

    /**
     * 发生延迟消息
     * @param destination topic + tag
     * @param message 消息
     * @param sendCallback 回调
     * @param delayLevel 延迟级别 1s 5s 10s 30s 1m; 2m 3m 4m 5m 6m; 7m 8m 9m 10m 20m; 30m 1h 2h
     */
    public void asyncSendDelay(@NonNull String destination, @NonNull String message, SendCallback sendCallback, int delayLevel){
        MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(message);
        String bid = setAndGetBid(messageBuilder);
        Message<String> build = messageBuilder.build();
        rocketMQTemplate.asyncSend(
                destination, build,
                new SendCallBackLogImpl(bid, destination, message, sendCallback),
                3000, delayLevel
        );
    }

    /**
     * 发生延迟消息
     * @param destination topic + tag
     * @param message 消息
     * @param delayLevel 延迟级别 1s 5s 10s 30s 1m; 2m 3m 4m 5m 6m; 7m 8m 9m 10m 20m; 30m 1h 2h
     */
    public void asyncSendDelay(@NonNull String destination, @NonNull String message, int delayLevel){
        MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(message);
        String bid = setAndGetBid(messageBuilder);
        Message<String> build = messageBuilder.build();
        rocketMQTemplate.asyncSend(
                destination, build,
                new SendCallBackLogImpl(bid, destination, message, null),
                3000, delayLevel
        );
    }
}
