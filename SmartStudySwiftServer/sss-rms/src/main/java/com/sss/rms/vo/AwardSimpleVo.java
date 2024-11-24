package com.sss.rms.vo;

import lombok.Data;

@Data
public class AwardSimpleVo {
    private long id;
    private String name;
    private String pic;
    private String description;
    private double points;
}
