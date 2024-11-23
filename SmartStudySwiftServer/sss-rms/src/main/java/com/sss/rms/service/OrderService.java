package com.sss.rms.service;

import com.sss.common.api.RException;
import com.sss.common.constant.RmsConst;
import com.sss.common.mapper.MQLogMapper;
import com.sss.common.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.sss.common.constant.RmsConst.AWARD_INVENTORY_PREFIX;
import static com.sss.common.constant.RmsConst.PLACE_ORDER_PREFIX;

@Slf4j
@Service
public class OrderService {

    @Resource
    private RedisService redisService;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private MQLogMapper mqLogMapper;

    /**
     * 用户下单
     * @param uid 用户id
     * @param awardId 奖品id
     */
    public void placeOrder(long uid, long awardId){
        String uk = RmsConst.generateOrderPlaceKey(uid, awardId); // 唯一key
        if(!redisService.setNx(PLACE_ORDER_PREFIX + uk, 0)){
            log.info("下单失败：已经兑换过该奖品 {} {}", uid, awardId);
            throw new RException("您已经兑换过该奖品，请看看其它奖品吧(～￣▽￣)～");
        }

        // 库存预扣减
        long inventory = redisService.decr(AWARD_INVENTORY_PREFIX + awardId, 1);
        if(inventory < 0){
            redisService.del(PLACE_ORDER_PREFIX + uk);
            log.info("下单失败：库存不够 {} {}", uid, awardId);
            throw new RException("该奖品已经被抢完了");
        }

        // 发送mq消息
        rocketMQTemplate.asyncSend(RmsConst.ORDER_PLACE_TOPIC, uk, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 日志记录：处理消息丢失
                mqLogMapper.sendSuccess(sendResult.getMsgId(), RmsConst.ORDER_PLACE_TOPIC, null, uk);
                log.info("下单成功: " + uk);
            }

            @Override
            public void onException(Throwable throwable) {
                // 日志记录：处理消息丢失
                mqLogMapper.sendFailed(RmsConst.ORDER_PLACE_TOPIC, null, uk);
                redisService.del(PLACE_ORDER_PREFIX + uk);
            }
        });
    }

}
