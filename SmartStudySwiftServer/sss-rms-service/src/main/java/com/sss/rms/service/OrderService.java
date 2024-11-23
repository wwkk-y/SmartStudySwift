package com.sss.rms.service;

import cn.hutool.core.lang.hash.Hash;
import com.sss.common.constant.RmsConst;
import com.sss.common.dao.RmsOrder;
import com.sss.rms.mapper.RmsAwardMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
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
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 下单
     * @param uid 用户id
     * @param awardId 奖品id
     */
    @Transactional(rollbackFor = Exception.class)
    public void placeOrder(long uid, long awardId){
        // 扣减库存
        int lines = awardMapper.decrementInventor(awardId);
        if(lines == 0){
            // 扣减库存失败 库存不够 | 商品下架
            throw new RuntimeException(); // 回滚
        }

        // 生成订单
        RmsOrder order = new RmsOrder();
        awardMapper.newOrder(uid, awardId, order);
        log.info(order.toString());

         // 订单超时自动取消消息
         rocketMQTemplate.asyncSend(
                 RmsConst.ORDER_CANCEL_TOPIC,
                 MessageBuilder.withPayload(order.getId())
                         .setHeader(RocketMQHeaders.DELAY, 16) // 设置延迟级别为16，即30分钟
                         .build(),
                 new SendCallback() {
                     @Override
                     public void onSuccess(SendResult sendResult) {
                         // TODO 日志
                     }

                     @Override
                     public void onException(Throwable throwable) {
                        // TODO 日志
                     }
                 }
         );
    }
}
