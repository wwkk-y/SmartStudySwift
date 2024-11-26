package com.sss.lms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/live/stream")
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter();
        // 模拟发送直播数据
        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    emitter.send(SseEmitter.event().data("直播数据" + i));
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        return emitter;
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello world";
    }
}
