package com.sss.common.dao;


public class UmsLoginLog {

  private long id;
  private String username;
  private String token;
  private long logout;
  private java.sql.Timestamp loginTime;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }


  public long getLogout() {
    return logout;
  }

  public void setLogout(long logout) {
    this.logout = logout;
  }


  public java.sql.Timestamp getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(java.sql.Timestamp loginTime) {
    this.loginTime = loginTime;
  }

}
