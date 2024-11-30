package com.sss.cms.controller;

import com.sss.cms.service.MessageService;
import com.sss.cms.vo.MessageQueryVo;
import com.sss.cms.vo.MessageSendVo;
import com.sss.common.api.RCode;
import com.sss.common.api.RException;
import com.sss.common.api.RResult;
import com.sss.security.dao.UmsUserDao;
import com.sss.security.util.AccountUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api("消息")
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @Resource
    private AccountUtil accountUtil;

    /**
     * 发消息
     */
    @ApiOperation("发消息，返回发出的消息")
    @PostMapping("/send")
    public RResult<MessageQueryVo> send(
            @Valid @RequestBody MessageSendVo msg
    ){
        return RResult.success(messageService.sendMsg(msg));
    }

    @ApiOperation("清除未读消息")
    @PostMapping("/clearUnreadMsg")
    public RResult<Boolean> clearUnreadMsg(
            @RequestParam long conversationId
    ){
        UmsUserDao account = accountUtil.getCurAccount();
        if(account == null){
            throw new RException(RCode.UNAUTHORIZED);
        }

        messageService.clearUnreadMsg(conversationId, account.getId());
        return RResult.success(true);
    }
}
