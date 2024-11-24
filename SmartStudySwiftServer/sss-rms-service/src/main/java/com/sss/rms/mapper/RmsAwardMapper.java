package com.sss.rms.mapper;

import com.sss.common.dao.RmsOrder;
import com.sss.rms.dao.RmsAwardInventoryDao;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RmsAwardMapper {

    @Select("select id, inventory from rms_award " +
            "where status = 1 and publish_status = 1 and verify_status = 1")
    List<RmsAwardInventoryDao> getAvailableAwardInventor();

    @Update("update rms_award " +
            "set inventory = inventory - 1 " +
            "where status = 1 and publish_status = 1 and verify_status = 1 " +
            "and id = #{awardId} and inventory > 0")
    int decrementAvailableInventor(@Param("awardId") long awardId);

    @Update("update rms_award " +
            "set inventory = inventory + 1 " +
            "where id = #{awardId}")
    int incrementInventory(@Param("awardId") long awardId);

    @Insert("insert into rms_order(user_id, award_id, state) " +
            "values (#{uid}, #{awardId}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "rmsOrder.id")
    void newOrder(@Param("uid") long uid, @Param("awardId") long awardId, @Param("rmsOrder") RmsOrder rmsOrder);

    /**
     * 当订单是待支付状态时取消订单
     * @param orderId 订单id
     */
    @Update("update rms_order set state = -2 " +
            "where id = #{orderId} " +
            "and status = 1 and state = 0")
    void cancelOrderIfPendingPayment(long orderId);

    @Select("select award_id from rms_order " +
            "where id = #{orderId} " +
            "and status = 1 and state = 0")
    Long geAwardIdOfPendingPaymentOrder(long orderId);
}
