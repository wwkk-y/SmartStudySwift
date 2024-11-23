package com.sss.rms.service;

import com.sss.common.constant.RmsConst;
import com.sss.common.service.RedisService;
import com.sss.rms.dao.RmsAwardInventoryDao;
import com.sss.rms.mapper.RmsAwardMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Service
public class ScheduleService {

    @Resource
    private RmsAwardMapper rmsAwardMapper;

    @Resource
    private RedisService redisService;

    /**
     * 定时同步奖品库存 mysql -> redis
     * 每天早上10点，晚上8点同步
     */
//    @Scheduled(
//            initialDelay = 1000, // 第一次执行在启动多少秒后
//            fixedDelay = 1000 // 间隔多少秒执行一次
//    )
    @Scheduled(
            cron = "0 0 10 * * ?" // 每天10点
            // （秒、分钟、小时、日期、月份、星期、年份，其中年份是可选的）
    )
    @PostConstruct // 后属性注入完毕后（项目启动）使用这个方法
    public void syncAwardInventory(){
        // 只运行一个线程同步
        if(!redisService.setNx(RmsConst.AWARD_INVENTORY_SYNC_LOCK, true)){
            return;
        }

        // 查询商品库存
        List<RmsAwardInventoryDao> awardList = rmsAwardMapper.getAvailableAwardInventor();
        if(awardList != null){
            awardList.forEach(award -> {
                // 如果业务逻辑很长，需要加锁
                redisService.set(RmsConst.AWARD_INVENTORY_PREFIX + award.getId(), award.getInventory());
            });
        }

        redisService.del(RmsConst.AWARD_INVENTORY_SYNC_LOCK);
    }
}
