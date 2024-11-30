package com.sss.ka.service;

import com.sss.common.api.RResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 发消息类，在集群里面找用户发消息
 * -> 往MQ里面推消息，集群里面MQ消费
 * TODO
 */
@Service
public class SendMsgService {

    public RResult<String> sendToUser(long uid, String path, String message){
        return null;
    }

    /**
     * 广播消息
     */
    public RResult<String> broadcast(String path, String message){
        return null;
    }


    public RResult<String> sendToUsers(List<Long> userIds, String path, String body){
        return null;
    }

}
