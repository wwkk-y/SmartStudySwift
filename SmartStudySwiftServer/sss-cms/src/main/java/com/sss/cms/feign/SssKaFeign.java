package com.sss.cms.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "sendMsgController")
public class SendMsgFeign {

}
