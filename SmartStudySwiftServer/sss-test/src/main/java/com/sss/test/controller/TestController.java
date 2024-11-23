package com.sss.test.controller;


import com.sss.common.api.RException;
import com.sss.common.api.RResult;
import com.sss.common.service.RedisService;
import com.sss.security.util.JWTUtil;
import com.sss.test.dao.TmpTest;
import com.sss.test.mapper.HereTmpTestMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private HereTmpTestMapper hereTmpTestMapper;

    @Resource
    private RedisService redisService;

    @Resource
    private JWTUtil jwtUtil;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping()
    public String helloWorld(){
        return "hello world";
    }

    @GetMapping("/MySQL")
    public List<TmpTest> MySQl(){
        return hereTmpTestMapper.queryAll();
    }

    @PostMapping("/redis/set/{key}/{value}")
    public String redisSet(@PathVariable String key, @PathVariable String value) {
        redisService.set(key, value);
        return "success";
    }

    @GetMapping("/redis/get/{key}")
    public String redisGet(@PathVariable String key){
        return (String) redisService.get(key);
    }

    @Cacheable(value = "testCache", key = "#key")
    @GetMapping("/cache/get/{key}")
    public String cacheGet(@PathVariable String key){
        log.info("cache get {}", key);
        return "execute";
    }

    @CacheEvict(value = "testCache", key = "#key")
    @PostMapping("/cache/del/{key}")
    public String cacheDel(@PathVariable String key){
        return "success";
    }


    @GetMapping("/error/{message}")
    public String error(@PathVariable String message){
        throw new RException(message);

//        return "success";
    }

    @PostMapping("/rocketMQ/send/")
    public String rocketMQSend(String msg){
        SendResult sendResult = rocketMQTemplate.syncSend("test-topic", msg);
        return sendResult.getSendStatus().toString();
    }

    @GetMapping("/json")
    public RResult<String> json(){
        return RResult.success("success");
    }

    @GetMapping("/timeWait/{ms}")
    public String timeWait(@PathVariable Long ms) throws InterruptedException {
        Thread.sleep(ms);
        return "success";
    }

}
