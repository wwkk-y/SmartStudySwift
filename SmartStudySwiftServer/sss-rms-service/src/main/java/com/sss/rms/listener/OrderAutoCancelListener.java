package com.sss.rms.listener;

import com.sss.common.constant.RmsConst;
import com.sss.common.constant.RocketMQConst;
import com.sss.common.util.RocketMQLogListener;
import com.sss.rms.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = RmsConst.ORDER_AUTO_CANCEL_TOPIC,
        consumerGroup = "order-auto-cancel-consumer-group",
        maxReconsumeTimes = RocketMQConst.RECONSUME_TIMES
)
public class OrderAutoCancelListener extends RocketMQLogListener {

    @Resource
    private OrderService orderService;

    @Override
    public void onLogMessage(MessageExt messageExt) {
        String body = new String(messageExt.getBody());
        long orderId = Long.parseLong(body);
        orderService.autoCancelOrder(orderId);
    }
}
