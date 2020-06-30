package com.winnie.demo.controller;

import com.winnie.demo.CustomSocketIOServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : winnie [wangliu023@qq.com]
 * @date : 2020/6/30
 * @desc
 */
@RestController
@RequestMapping("/socket")
public class SocketController {

    @Resource
    private CustomSocketIOServer customSocketIOServer;

    @GetMapping("/send/room1")
    public void sendMessage1(String data){
        customSocketIOServer.sendRoom1Event(data);
    }

    @GetMapping("/send/room2")
    public void sendMessage2(String data){
        customSocketIOServer.sendRoom2Event(data);
    }
}
