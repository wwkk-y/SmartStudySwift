package com.sss.rms.listener;

import com.sss.common.constant.RmsConst;
import com.sss.common.constant.RocketMQConst;
import com.sss.common.util.RocketMQLogListener;
import com.sss.rms.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = RmsConst.ORDER_PLACE_TOPIC,
        consumerGroup = "order-place-consumer-group",
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadNumber = 40,
        maxReconsumeTimes = RocketMQConst.RECONSUME_TIMES
)
public class OrderPlaceListener extends RocketMQLogListener {

    @Resource
    private OrderService orderService;

    @Override
    public void onLogMessage(MessageExt messageExt) {
        String s = new String(messageExt.getBody());
        long[] ids = RmsConst.parseOrderPlaceKey(s);
        if(ids == null){
            // 消费异常日志
            log.warn("下单消息：参数解析错误 {}", s);
            throw new RuntimeException("参数解析错误");
        }

        orderService.placeOrder(ids[0], ids[1]);
    }
}

