package com.sss.common.dao;


public class QmsKnowledgePointStructLink {

  private long id;
  private long knowledgeStructId;
  private long subjectId;
  private long stageId;
  private long knowledgePointId;
  private long sort;
  private long parentId;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getKnowledgeStructId() {
    return knowledgeStructId;
  }

  public void setKnowledgeStructId(long knowledgeStructId) {
    this.knowledgeStructId = knowledgeStructId;
  }


  public long getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(long subjectId) {
    this.subjectId = subjectId;
  }


  public long getStageId() {
    return stageId;
  }

  public void setStageId(long stageId) {
    this.stageId = stageId;
  }


  public long getKnowledgePointId() {
    return knowledgePointId;
  }

  public void setKnowledgePointId(long knowledgePointId) {
    this.knowledgePointId = knowledgePointId;
  }


  public long getSort() {
    return sort;
  }

  public void setSort(long sort) {
    this.sort = sort;
  }


  public long getParentId() {
    return parentId;
  }

  public void setParentId(long parentId) {
    this.parentId = parentId;
  }

}
