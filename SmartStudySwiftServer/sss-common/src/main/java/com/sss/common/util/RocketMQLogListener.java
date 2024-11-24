package com.sss.common.util;

import com.sss.common.constant.RocketMQConst;
import com.sss.common.mapper.GlobalSetMapper;
import com.sss.common.mapper.MQLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.dao.DuplicateKeyException;

import javax.annotation.Resource;

/**
 * 代理模式，MQ消费者接受消息时，在原来的逻辑上加上了日志处理
 */
@Slf4j
public abstract class RocketMQLogListener implements RocketMQListener<MessageExt> {
    @Resource
    private GlobalSetMapper globalSetMapper;
    @Resource
    private MQLogMapper mqLogMapper;

    @Override
    public void onMessage(MessageExt msg) {
        // 去重表 避免重复消费
        String bid = msg.getKeys();
        if(bid == null){
            log.error("RocketMQ 消息 keys 不能为 null; msgId = {}", msg.getMsgId());
            return;
        }
        try{
            globalSetMapper.insert(bid);
        } catch (DuplicateKeyException e){
            log.warn("下单消息：重复消费 bid = {}", bid);
            return;
        }

        // 开始消费日志
        int circleTimes = 100; // 100 * 50 = 5000ms
        while(circleTimes > 0){ // 自旋锁 获取日志行
            int lines = mqLogMapper.consumeStart(msg.getMsgId());
            if(lines == 0){
                // WAL 先写日志，保证日志先写进去了
                try {
                    //
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                break;
            }

            circleTimes -= 1;
        }

        // 获取日志行失败
        if(circleTimes == 0){
            mqLogMapper.WALError(
                    msg.getMsgId(), bid,
                    String.format("topic: '%s', tags: '%s'", msg.getTopic(), msg.getTags()),
                    new String(msg.getBody())
            );
            return;
        }

        try{
            // 执行业务
            onLogMessage(msg);
        } catch (Exception e){
            // 发生异常，消费出错日志
            mqLogMapper.consumeError(msg.getMsgId(), e.getMessage(), msg.getReconsumeTimes());

            // 后续需要重试时，删除去重表消息便于重复消费
            if(msg.getReconsumeTimes() < RocketMQConst.RECONSUME_TIMES){
                // 小于重试次数次，删除去重表消息，因为后面需要重试
                globalSetMapper.delete(bid);
            }
            throw e;
        }
        mqLogMapper.consumeSuccess(msg.getMsgId()); // 消费成功日志
    }


    public abstract void onLogMessage(MessageExt messageExt);

}
