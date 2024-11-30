package com.sss.common.dao;


public class QmsQuestionKnowledgePoint {

  private long id;
  private long questionId;
  private String knowledgePointRange;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(long questionId) {
    this.questionId = questionId;
  }


  public String getKnowledgePointRange() {
    return knowledgePointRange;
  }

  public void setKnowledgePointRange(String knowledgePointRange) {
    this.knowledgePointRange = knowledgePointRange;
  }

}
