package com.sss.common.vo;

import lombok.Data;

@Data
public class UmsUserVo {
    private long id;
    private String username;
    private String icon;
    private String email;
    private String nickName;
    private String note;
    private java.sql.Timestamp loginTime;
}