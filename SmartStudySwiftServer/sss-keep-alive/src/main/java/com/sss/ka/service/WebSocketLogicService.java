package com.sss.ka.service;

import com.sss.ka.vo.WebSocketLogicMsg;
import com.sss.ka.vo.WebSocketToClientMsg;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * websocket业务逻辑处理
 */
@Service
public class WebSocketLogicService {

    public void testBroadcast(@NonNull WebSocketLogicMsg msg){
        WebSocketService.broadcastMessageToClient(WebSocketToClientMsg.builder()
                .body("hello this is " + msg.getUser().getUsername()).build()
        );
    }

}
