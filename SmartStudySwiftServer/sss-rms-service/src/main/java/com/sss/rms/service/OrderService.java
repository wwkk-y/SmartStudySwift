package com.sss.rms.service;

import com.sss.rms.mapper.RmsAwardMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class OrderService {

    @Resource
    private RmsAwardMapper awardMapper;

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
        awardMapper.newOrder(uid, awardId);
    }
}
