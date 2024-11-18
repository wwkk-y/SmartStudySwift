package com.sss.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/logged")
public class LoggedTestController {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/role1")
    @RolesAllowed("ADMIN")
    public String roleUser(){
        return "success";
    }
}
