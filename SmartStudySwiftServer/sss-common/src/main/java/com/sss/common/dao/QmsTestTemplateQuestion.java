package com.sss.common.dao;


public class QmsTestTemplateQuestion {

  private long id;
  private long testTemplateStructId;
  private String knowledgePointRange;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getTestTemplateStructId() {
    return testTemplateStructId;
  }

  public void setTestTemplateStructId(long testTemplateStructId) {
    this.testTemplateStructId = testTemplateStructId;
  }


  public String getKnowledgePointRange() {
    return knowledgePointRange;
  }

  public void setKnowledgePointRange(String knowledgePointRange) {
    this.knowledgePointRange = knowledgePointRange;
  }

}
