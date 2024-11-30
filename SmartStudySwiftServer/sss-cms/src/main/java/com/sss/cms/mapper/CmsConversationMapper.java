package com.sss.cms.mapper;

import com.sss.cms.vo.ConversationListVo;
import com.sss.cms.vo.LastMessageVo;
import com.sss.cms.vo.MessageQueryVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CmsConversationMapper {
    /**
     * 查询某个用户的会话，按照 update_time 排序
     * @param uid id
     * @param targetName 可为空, 对方名字
     */
    List<ConversationListVo> conversationListOfUser(@Param("uid") long uid, @Param("targetName") String targetName);

    /**
     * 查询某个用户的会话列表项信息
     */
    ConversationListVo conversationInfo(@Param("uid") long uid, @Param("conversationId") long conversationId);

    /**
     * 查询某个用户某个会话未读消息数量
     */
    long getUnreadMsgNum(@Param("uid") long uid, @Param("conversationId") long conversationId);

    /**
     * 查询最后一条信息内容
     * @param msgId
     * @return
     */
    LastMessageVo getLastMsgInfo(long msgId);

    @Select("select count(*) from cms_conversation where id = #{conversationId} " +
            "and (user_id1 = #{uid} or user_id2 = #{uid}) limit 1")
    int hasConversation(@Param("uid") long uid, @Param("conversationId") long conversationId);

    @Select("select id, sender_id, type, content, create_time " +
            "from cms_message where conversation_id = #{conversationId} " +
            "order by create_time desc")
    List<MessageQueryVo> messageListOfConversation(long conversationId);

    @Select("select id from cms_conversation " +
            "where (user_id1 = #{userId1} and user_id2 = #{userId2}) " +
            "or (user_id1 = #{userId2} and user_id2 = #{userId1}) " +
            "limit 1")
    Long getConversationIdByUser(@Param("userId1") long userId1, @Param("userId2") long userId2);

    @Insert("insert into cms_conversation(user_id1, user_id2) value (#{selfUid}, #{targetUid})")
    void createConversation(@Param("selfUid") long selfUid, @Param("targetUid") long targetUid);
}
