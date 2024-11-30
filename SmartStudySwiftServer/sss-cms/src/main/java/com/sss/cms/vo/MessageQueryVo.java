package com.sss.cms.vo;

import lombok.Data;

@Data
public class MessageQueryVo {
    private long id;
    private long senderId;
    private long conversationId;
    private String type;
    private String content;
    private java.sql.Timestamp createTime;
}
