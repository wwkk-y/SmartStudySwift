package com.sss.ka.vo;

import com.sss.security.dao.UmsUserDao;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * websocket 业务逻辑消息
 * 客户端 -> 服务端 --解析--> 业务逻辑
 */
@Data
@Builder
public class WebSocketLogicMsg {
    private UmsUserDao user;
    private List<String> permissions;
    private String path;
    private String body;
}
