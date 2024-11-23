package com.sss.common.dao;


public class RmsAward {

  private long id;
  private String name;
  private String pic;
  private String description;
  private long inventory;
  private double points;
  private long publishStatus;
  private long verifyStatus;
  private long sort;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;
  private long status;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getPic() {
    return pic;
  }

  public void setPic(String pic) {
    this.pic = pic;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public long getInventory() {
    return inventory;
  }

  public void setInventory(long inventory) {
    this.inventory = inventory;
  }


  public double getPoints() {
    return points;
  }

  public void setPoints(double points) {
    this.points = points;
  }


  public long getPublishStatus() {
    return publishStatus;
  }

  public void setPublishStatus(long publishStatus) {
    this.publishStatus = publishStatus;
  }


  public long getVerifyStatus() {
    return verifyStatus;
  }

  public void setVerifyStatus(long verifyStatus) {
    this.verifyStatus = verifyStatus;
  }


  public long getSort() {
    return sort;
  }

  public void setSort(long sort) {
    this.sort = sort;
  }


  public java.sql.Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.sql.Timestamp createTime) {
    this.createTime = createTime;
  }


  public java.sql.Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(java.sql.Timestamp updateTime) {
    this.updateTime = updateTime;
  }


  public long getStatus() {
    return status;
  }

  public void setStatus(long status) {
    this.status = status;
  }

}
