package com.sss.cms.vo;

import lombok.Data;

@Data
public class MessageListVo {
    private long id;
    private long senderId;
    private String type;
    private String content;
    private java.sql.Timestamp createTime;
}
