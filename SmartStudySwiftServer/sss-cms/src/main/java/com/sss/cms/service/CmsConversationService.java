package com.sss.cms.service;

import com.sss.cms.mapper.CmsConversationMapper;
import com.sss.cms.vo.ConversationListVo;
import com.sss.cms.vo.MessageQueryVo;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CmsConversationService {

    @Resource
    private CmsConversationMapper cmsConversationMapper;

    /**
     * 查询某个用户的会话，按照 update_time 排序
     * @param uid id
     * @param targetName 可为空, 对方名字
     */
    public List<ConversationListVo> listOfUser(long uid, String targetName){
        List<ConversationListVo> conversationList = cmsConversationMapper.conversationListOfUser(uid, targetName);

        conversationList.forEach(conversation -> queryConversationExtMsg(uid, conversation));

        return conversationList;
    }

    /**
     * 查询某个用户的会话额外信息
     */
    private void queryConversationExtMsg(long uid, @NonNull ConversationListVo conversation){
        // 查询未读消息
        conversation.setUnreadMsgNum(cmsConversationMapper.getUnreadMsgNum(uid, conversation.getId()));
        // 查询最后一条消息
        if(conversation.getLastMsgId() != null){
            conversation.setLastMsg(cmsConversationMapper.getLastMsgInfo(conversation.getLastMsgId()));
        }
    }

    /**
     * 查询某个用户的会话列表项信息
     */
    public ConversationListVo conversationInfo(long uid, long conversationId) {
        ConversationListVo conversationInfo = cmsConversationMapper.conversationInfo(uid, conversationId);
        if(conversationInfo != null){
            queryConversationExtMsg(uid, conversationInfo);
        }
        return conversationInfo;
    }

    public boolean hasConversation(long uid, long conversationId){
        return cmsConversationMapper.hasConversation(uid, conversationId) > 0;
    }

    public List<MessageQueryVo> messageListOfConversation(long conversationId){
        // 查询消息
        return cmsConversationMapper.messageListOfConversation(conversationId);
    }


    /**
     * 如果会话不存在，建立这个会话，存在 -> 返回会话详情
     * @param selfUid 自己的id
     * @param targetUid 对方的id
     * @return 会话详情
     */
    public ConversationListVo tryConversationWithUser(long selfUid, long targetUid) {
        // 查询会话是否存在
        Long conversationId = cmsConversationMapper.getConversationIdByUser(selfUid, targetUid);
        if(conversationId == null){
            // 不存在就创建
            cmsConversationMapper.createConversation(selfUid, targetUid);
            // 拿到 conversationId
            conversationId = cmsConversationMapper.getConversationIdByUser(selfUid, targetUid);
        }
        return conversationInfo(selfUid, conversationId);
    }
}
