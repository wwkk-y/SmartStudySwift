package com.sss.cms.feign;

import com.sss.common.api.RResult;
import com.sss.common.vo.KaMsgSendVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "sss-keep-alive") // name 服务名
public interface SssKaFeign {
    @PostMapping("/public/msg/send")
    RResult<Boolean> send(KaMsgSendVo msgSendVo);
}
