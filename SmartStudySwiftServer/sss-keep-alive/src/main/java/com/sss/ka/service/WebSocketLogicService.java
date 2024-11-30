package com.sss.ka.service;

import com.sss.ka.vo.WebSocketLogicMsg;
import com.sss.ka.vo.WebSocketToClientMsg;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * websocket业务逻辑处理
 */
@Slf4j
@Service
public class WebSocketLogicService {

    public void testBroadcast(@NonNull WebSocketLogicMsg msg){
        WebSocketService.broadcastMessageToClient(WebSocketToClientMsg.builder()
                .body("hello this is " + msg.getUser().getUsername()).build()
        );
    }

    // 做权限校验的空方法
    public void connect(@NonNull WebSocketLogicMsg msg){
        log.info("websocket connect: {}, {}", msg.getUser(), msg.getPermissions());
    }

}
