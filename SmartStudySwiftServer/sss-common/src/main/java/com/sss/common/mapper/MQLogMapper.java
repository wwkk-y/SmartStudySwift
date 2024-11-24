package com.sss.common.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface MQLogMapper {
    @Insert("insert into mq_log(messageId, bid, destination, message, state) " +
            "values (#{messageId}, #{bid}, #{destination}, #{message}, 0)")
    void sendSuccess(
            @Param("messageId") String messageId, @Param("bid") String bid,
            @Param("destination") String destination, @Param("message") String message
    );

    @Insert("insert into mq_log(bid, destination, message, state, error_msg) " +
            "values (#{bid}, #{destination}, #{message}, 1, #{errorMsg})")
    void sendFailed(
            @Param("bid") String bid,
            @Param("destination") String destination,
            @Param("message") String message, @Param("errorMsg") String errorMsg
    );

    @Insert("insert into mq_log(messageId, bid, destination, message, state) " +
            "values (#{messageId}, #{bid}, #{destination}, #{message}, 5)"
    )
    void WALError(
            @Param("messageId") String messageId, @Param("bid") String bid,
            @Param("destination") String destination,
            @Param("message") String message
    );

    @Update("update mq_log set state = 2 where messageId = #{messageId}")
    int consumeStart(@Param("messageId") String messageId);

    @Update("update mq_log set state = 3 where messageId = #{messageId}")
    void consumeSuccess(@Param("messageId") String messageId);

    @Update("update mq_log set state = 4, error_msg = #{errorMsg}, reconsume_times = #{reConsumeTimes} " +
            "where messageId = #{messageId}")
    void consumeError(
            @Param("messageId") String messageId,
            @Param("errorMsg") String errorMsg,
            @Param("reConsumeTimes") int reConsumeTimes
    );
}
