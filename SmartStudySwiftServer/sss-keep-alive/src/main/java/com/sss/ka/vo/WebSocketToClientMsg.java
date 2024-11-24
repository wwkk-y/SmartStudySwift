package com.sss.ka.vo;


import lombok.Builder;
import lombok.Data;

/**
 * websocket消息
 * 服务端 -> 客户端
 */
@Data
@Builder
public class WebSocketToClientMsg {
    private int code; // 状态码
    private String path; // 路由
    private String body; // 具体消息
}
