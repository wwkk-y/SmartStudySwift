package com.sss.rms.listener;

import com.sss.common.constant.RmsConst;
import com.sss.common.mapper.GlobalSetMapper;
import com.sss.common.mapper.MQLogMapper;
import com.sss.rms.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = RmsConst.ORDER_PLACE_TOPIC,
        consumerGroup = "order-place-consumer-group",
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadNumber = 40
)
public class OrderPlaceListener implements RocketMQListener<MessageExt> {

    @Resource
    private OrderService orderService;

    @Resource
    private GlobalSetMapper globalSetMapper;

    @Resource
    private MQLogMapper mqLogMapper;

    /**
     * 处理下单消息
     */
    @Override
    public void onMessage(MessageExt msg) {
        // 去重表 避免重复消费
        String s = new String(msg.getBody());
        try{
            globalSetMapper.insert(RmsConst.PLACE_ORDER_PREFIX + s);
        } catch (DuplicateKeyException e){
            log.warn("下单消息：重复消费 {}", s);
            return;
        }

        // 开始消费日志
        mqLogMapper.consumeStart(msg.getMsgId());

        long[] ids = RmsConst.parseOrderPlaceKey(s);
        if(ids == null){
            // 消费异常日志
            mqLogMapper.consumeError(msg.getMsgId(), "消息解析错误");
            log.warn("下单消息：异常消息未处理 {}", s);
            return;
        }

        try{
            // 执行业务
            orderService.placeOrder(ids[0], ids[1]);
        } catch (Exception e){
            // 发生异常，消费出错日志
            mqLogMapper.consumeError(msg.getMsgId(), e.getMessage());
            throw e;
        }

        mqLogMapper.consumeSuccess(msg.getMsgId()); // 消费成功日志
    }
}
