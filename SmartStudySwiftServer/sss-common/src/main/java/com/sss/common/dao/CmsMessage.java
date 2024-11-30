package com.sss.common.dao;


public class CmsMessage {

  private long id;
  private long conversationId;
  private String type;
  private String content;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getConversationId() {
    return conversationId;
  }

  public void setConversationId(long conversationId) {
    this.conversationId = conversationId;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
