package com.sss.common.dao;


public class QmsStageSysStageLink {

  private long id;
  private long stageSysId;
  private long stageId;
  private long stageSort;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getStageSysId() {
    return stageSysId;
  }

  public void setStageSysId(long stageSysId) {
    this.stageSysId = stageSysId;
  }


  public long getStageId() {
    return stageId;
  }

  public void setStageId(long stageId) {
    this.stageId = stageId;
  }


  public long getStageSort() {
    return stageSort;
  }

  public void setStageSort(long stageSort) {
    this.stageSort = stageSort;
  }

}
