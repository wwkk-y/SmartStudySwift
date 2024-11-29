package com.sss.lms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("会话")
@RequestMapping("/ums/conversation")
public class CmsConversationController {

    @ApiOperation("获取当前用户的会话，按照时间排序")
    @GetMapping("/list")

}
