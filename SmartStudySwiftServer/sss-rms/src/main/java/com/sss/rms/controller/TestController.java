package com.sss.rms.controller;

import com.sss.rms.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private OrderService orderService;

    @PostMapping("/order/place/{awardId}")
    public void orderPlace(@PathVariable Long awardId){
        Random random = new Random();
        int uid = random.nextInt(2000000);
        log.info("测试下单 {} {}", uid, awardId);
        orderService.placeOrder(uid, awardId);
    }
}
