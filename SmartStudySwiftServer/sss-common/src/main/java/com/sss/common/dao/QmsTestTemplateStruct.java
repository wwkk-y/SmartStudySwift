package com.sss.common.dao;


public class QmsTestTemplateStruct {

  private long id;
  private long qmsTestTemplateId;
  private long questionTypeId;
  private long sort;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getQmsTestTemplateId() {
    return qmsTestTemplateId;
  }

  public void setQmsTestTemplateId(long qmsTestTemplateId) {
    this.qmsTestTemplateId = qmsTestTemplateId;
  }


  public long getQuestionTypeId() {
    return questionTypeId;
  }

  public void setQuestionTypeId(long questionTypeId) {
    this.questionTypeId = questionTypeId;
  }


  public long getSort() {
    return sort;
  }

  public void setSort(long sort) {
    this.sort = sort;
  }

}
