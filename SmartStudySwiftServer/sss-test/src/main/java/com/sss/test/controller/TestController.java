package com.sss.test.controller;


import com.sss.common.service.RedisService;
import com.sss.test.dao.TmpTest;
import com.sss.test.mapper.HereTmpTestMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private HereTmpTestMapper hereTmpTestMapper;

    @Resource
    private RedisService redisService;

    @GetMapping
    public String helloWorld(){
        return "hello world";
    }

    @GetMapping("/MySQL")
    public List<TmpTest> MySQl(){
        return hereTmpTestMapper.queryAll();
    }

    @RequestMapping("/redis/set/{key}/{value}")
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
    @RequestMapping("/cache/del/{key}")
    public String cacheDel(@PathVariable String key){
        return "success";
    }

}
