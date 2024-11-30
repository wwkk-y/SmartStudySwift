package com.sss.ka.vo;

import lombok.Data;

import java.util.List;

/**
 * websocket mq 消费者处理的消息
 * 服务端 <-> 服务端
 */
@Data
public class WebSocketToMQConsumerMsg {
    public boolean broadcast; // 是否广播
    public List<Long> userIds; // 需要发消息的用户
    public WebSocketToClientMsg msg; // 发送给客户端的消息
}
