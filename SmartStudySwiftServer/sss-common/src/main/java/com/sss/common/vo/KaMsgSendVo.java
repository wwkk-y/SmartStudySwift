package com.sss.common.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class KaMsgSendVo {
    @NotNull
    private List<Long> userIds;
    @NotNull
    private String path;
    private String body;
}
