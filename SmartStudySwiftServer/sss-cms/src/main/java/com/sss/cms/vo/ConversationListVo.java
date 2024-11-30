package com.sss.cms.vo;

import lombok.Data;

@Data
public class ConversationListVo {
    private long id;
    private java.sql.Timestamp updateTime;
    // 对方信息
    private long userId;
    private String icon;
    private String nickName;
    private String username;
    // 未读消息数量
    private long unreadMsgNum;
    // 最后一条信息
    private Long lastMsgId;
    private LastMessageVo lastMsg;
}
