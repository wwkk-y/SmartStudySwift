package com.sss.cms.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class MessageSendVo {
    @Null
    private Long id;
    private Long senderId;
    @NotNull
    private Long conversationId;
    @NotEmpty
    private String type;
    @NotEmpty
    private String content;
}
