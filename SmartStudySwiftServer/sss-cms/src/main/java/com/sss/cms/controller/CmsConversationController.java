package com.sss.cms.controller;

import com.github.pagehelper.PageHelper;
import com.sss.cms.service.CmsConversationService;
import com.sss.cms.vo.ConversationListVo;
import com.sss.cms.vo.MessageQueryVo;
import com.sss.common.api.RCode;
import com.sss.common.api.RPage;
import com.sss.common.api.RResult;
import com.sss.security.dao.UmsUserDao;
import com.sss.security.util.AccountUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api("会话")
@RequestMapping("/cms/conversation")
public class CmsConversationController {

    @Resource
    private AccountUtil accountUtil;

    @Resource
    private CmsConversationService cmsConversationService;


    @ApiOperation("获取当前用户的会话，按照更新时间排序")
    @GetMapping("/list")
    public RResult<RPage<ConversationListVo>> list(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) String targetName
    ){
        UmsUserDao account = accountUtil.getCurAccount();
        if(account == null){
            return RResult.failed(RCode.UNAUTHORIZED);
        }

        PageHelper.startPage(pageNum, pageSize);

        return RResult.success(RPage.restPage(cmsConversationService.listOfUser(account.getId(), targetName)));
    }

    @ApiOperation("查询某个用户的会话信息")
    @GetMapping("/info")
    public RResult<ConversationListVo> info(
            @RequestParam long conversationId
    ){
        UmsUserDao account = accountUtil.getCurAccount();
        if(account == null){
            return RResult.failed(RCode.UNAUTHORIZED);
        }

        return RResult.success(cmsConversationService.conversationInfo(account.getId(), conversationId));
    }

    @ApiOperation("获取某个会话里的消息, 按照时间排序 -> 倒序")
    @GetMapping("/msg/list")
    public RResult<RPage<MessageQueryVo>> msgList(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam long conversationId
    ){
        UmsUserDao account = accountUtil.getCurAccount();
        if(account == null){
            return RResult.failed(RCode.UNAUTHORIZED);
        }

        // 查找会话，判断当前用户是否会话里的成员
        if (!cmsConversationService.hasConversation(account.getId(), conversationId)) {
            return RResult.failed("没有权限访问该会话");
        }

        PageHelper.startPage(pageNum, pageSize);
        return RResult.success(RPage.restPage(cmsConversationService.messageListOfConversation(conversationId)));
    }

    @ApiOperation("如果会话不存在，建立这个会话，存在 -> 返回会话详情")
    @PostMapping("/tryConversationWithUser")
    public RResult<ConversationListVo> tryConversationWithUser(@RequestParam long targetUid){
        UmsUserDao account = accountUtil.getCurAccount();
        if(account == null){
            return RResult.failed(RCode.UNAUTHORIZED);
        }

        if(targetUid == account.getId()){
            return RResult.failed("不能跟自己建立会话");
        }

        return RResult.success(cmsConversationService.tryConversationWithUser(account.getId(), targetUid));
    }
}
