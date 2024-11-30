package com.sss.common.dao;


public class QmsQuestion {

  private long id;
  private String title;
  private String answer;
  private long typeId;
  private long awardVal;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }


  public long getTypeId() {
    return typeId;
  }

  public void setTypeId(long typeId) {
    this.typeId = typeId;
  }


  public long getAwardVal() {
    return awardVal;
  }

  public void setAwardVal(long awardVal) {
    this.awardVal = awardVal;
  }

}
