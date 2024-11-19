package com.sss.test.controller;


import com.sss.common.api.RException;
import com.sss.common.service.RedisService;
import com.sss.security.util.JWTUtil;
import com.sss.test.dao.TmpTest;
import com.sss.test.mapper.HereTmpTestMapper;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping
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
}
