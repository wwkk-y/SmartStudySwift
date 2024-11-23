package com.sss.rms.controller;

import com.sss.common.api.RCode;
import com.sss.common.api.RException;
import com.sss.common.api.RResult;
import com.sss.common.service.RedisService;
import com.sss.common.constant.RmsConst;
import com.sss.rms.service.OrderService;
import com.sss.security.dao.UmsUserDao;
import com.sss.security.util.AccountUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.sss.common.constant.RmsConst.*;

@Slf4j
@RestController
@RequestMapping("/order")
@Api("订单相关")
public class OrderController {

    @Resource
    private AccountUtil accountUtil;

    @Resource
    private OrderService orderService;

    @PostMapping("/place")
    @ApiOperation("下单, 下单成功返回true")
    public RResult<String> place(HttpServletRequest request, @RequestParam long awardId){
        UmsUserDao curAccount = accountUtil.getCurAccount();
        if(curAccount == null){
            throw new RException(RCode.UNAUTHORIZED);
        }

        orderService.placeOrder(curAccount.getId(), awardId);

        return RResult.success("正在生成订单，请稍后去订单中心查看");
    }

}
