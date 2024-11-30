package com.sss.common.dao;


import java.sql.Timestamp;

public class CmsConversation {

  private long id;
  private long lastMsgId;
  private long subclassId;
  private long userId1;
  private long userId2;

  private java.sql.Timestamp updateTime;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getLastMsgId() {
    return lastMsgId;
  }

  public void setLastMsgId(long lastMsgId) {
    this.lastMsgId = lastMsgId;
  }


  public long getSubclassId() {
    return subclassId;
  }

  public void setSubclassId(long subclassId) {
    this.subclassId = subclassId;
  }


  public long getUserId1() {
    return userId1;
  }

  public void setUserId1(long userId1) {
    this.userId1 = userId1;
  }


  public long getUserId2() {
    return userId2;
  }

  public void setUserId2(long userId2) {
    this.userId2 = userId2;
  }

  public Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Timestamp updateTime) {
    this.updateTime = updateTime;
  }
}
