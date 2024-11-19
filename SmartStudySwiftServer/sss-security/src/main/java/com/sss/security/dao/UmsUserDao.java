package com.sss.security.dao;

import lombok.Data;

@Data
public class UmsUserDao {
    private long id;
    private String username;
    private String password;
    private String icon;
    private String email;
    private String nickName;
    private String note;
    private java.sql.Timestamp loginTime;
}