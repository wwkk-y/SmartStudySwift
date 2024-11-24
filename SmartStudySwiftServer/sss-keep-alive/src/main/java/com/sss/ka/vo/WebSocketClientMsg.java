package com.sss.ka.vo;

import lombok.Builder;
import lombok.Data;

/**
 * websocket 客户端消息
 * 客户端 -> 服务端
 */
@Data
@Builder
public class WebSocketClientMsg {
    private String authorization; // token
    private String path; // 路由
    private String body; // 具体消息
}
