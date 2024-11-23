package com.sss.common.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface MQLogMapper {
    @Insert("insert into mq_log(messageId, topic, tag, message, state) " +
            "values (#{messageId}, #{topic}, #{tag}, #{message}, 0)")
    void sendSuccess(
            @Param("messageId") String messageId,
            @Param("topic") String topic, @Param("tag") String tag, @Param("message") String message
    );

    @Insert("insert into mq_log(topic, tag, message, state) " +
            "values (#{topic}, #{tag}, #{message}, 1)")
    void sendFailed(@Param("topic") String topic, @Param("tag") String tag, @Param("message") String message);

    @Update("update mq_log set state = 2 where messageId=#{messageId}")
    void consumeStart(@Param("messageId") String messageId);

    @Update("update mq_log set state = 3 where messageId=#{messageId}")
    void consumeSuccess(@Param("messageId") String messageId);

    @Update("update mq_log set state = 4 and error_msg = #{errorMsg} where messageId=#{messageId}")
    void consumeError(@Param("messageId") String messageId, @Param("errorMsg") String errorMsg);
}
