package com.sss.cms.mapper;

import com.sss.cms.vo.MessageQueryVo;
import com.sss.cms.vo.MessageSendVo;
import com.sss.cms.vo.UserOfConversationVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface MessageMapper {

    @Insert("insert into cms_message(sender_id, conversation_id, type, content) " +
            "value (#{senderId}, #{conversationId}, #{type}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void newMessage(@Valid MessageSendVo messageSendVo);

    @Insert("insert into cms_unread_message(message_id, user_id) value (#{msgId}, #{uid})")
    void newUnreadMessage(@Param("msgId") long msgId, @Param("uid") long uid);

    @Update("update cms_conversation " +
            "set last_msg_id = #{msgId} where id = #{conversationId}")
    int setLastMsgOfConversation(@Param("msgId") long msgId, @Param("conversationId") long conversationId);

    @Select("select user_id1, user_id2 from cms_conversation where id = #{conversationId}")
    UserOfConversationVo queryUserOfConversation(long conversationId);

    @Select("select id, sender_id, conversation_id, type, content, create_time " +
            "from cms_message where id = #{msgId}")
    MessageQueryVo queryMsgById(@Param("msgId") long msgId);

    void clearUnreadMsg(@Param("conversationId") long conversationId, @Param("uid") long uid);
}
