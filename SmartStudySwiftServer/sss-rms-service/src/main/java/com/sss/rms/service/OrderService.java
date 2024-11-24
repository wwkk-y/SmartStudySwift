package com.sss.rms.service;

import com.sss.common.constant.RmsConst;
import com.sss.common.dao.RmsOrder;
import com.sss.common.util.RocketMQSendUtil;
import com.sss.rms.mapper.RmsAwardMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Slf4j
@Service
public class OrderService {

    @Resource
    private RmsAwardMapper awardMapper;

    @Resource
    private RocketMQSendUtil rocketMQSendUtil;

    /**
     * 下单
     * @param uid 用户id
     * @param awardId 奖品id
     */
    @Transactional(rollbackFor = Exception.class)
    public void placeOrder(long uid, long awardId){
        // 扣减库存
        int lines = awardMapper.decrementAvailableInventor(awardId);
        if(lines == 0){
            // 扣减库存失败 库存不够 | 商品下架
            throw new RuntimeException(); // 回滚
        }

        // 生成订单
        RmsOrder order = new RmsOrder();
        awardMapper.newOrder(uid, awardId, order);

         // 订单超时自动取消消息
        rocketMQSendUtil.asyncSendDelay(
                RmsConst.ORDER_AUTO_CANCEL_TOPIC,
                String.valueOf(order.getId()),
                16 // 设置延迟级别为16，即30分钟
         );
    }

    /**
     * 如果订单状态是待支付，自动取消订单，库存回退
     * @param orderId 订单id
     */
    @Transactional(rollbackFor = Exception.class)
    public void autoCancelOrder(long orderId){
        Long awardId = awardMapper.geAwardIdOfPendingPaymentOrder(orderId);
        if(awardId == null){
            log.warn("自动取消订单失败: {}", orderId);
            return;
        }
        // 取消订单
        awardMapper.cancelOrderIfPendingPayment(orderId);
        // 库存+1
        awardMapper.incrementInventory(awardId);
    }
}
