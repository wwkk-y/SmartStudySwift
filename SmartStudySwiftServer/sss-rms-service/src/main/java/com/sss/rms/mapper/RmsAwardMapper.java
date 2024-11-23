package com.sss.rms.mapper;

import com.sss.rms.dao.RmsAwardInventoryDao;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface RmsAwardMapper {

    @Select("select id, inventory from rms_award " +
            "where status = 1 and publish_status = 1 and verify_status = 1")
    List<RmsAwardInventoryDao> getAvailableAwardInventor();

    @Update("update rms_award " +
            "set inventory = inventory - 1 " +
            "where status = 1 and publish_status = 1 and verify_status = 1 " +
            "and id = #{awardId} and inventory > 0")
    int decrementInventor(@Param("awardId") long awardId);

    @Insert("insert into rms_order(user_id, award_id, state) " +
            "values (#{uid}, #{awardId}, 0)")
    void newOrder(@Param("uid") long uid, @Param("awardId") long awardId);
}
