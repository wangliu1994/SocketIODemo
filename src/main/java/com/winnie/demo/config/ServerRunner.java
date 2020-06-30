package com.winnie.demo.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author : winnie [wangliu023@qq.com]
 * @date : 2020/4/29
 * @desc
 */
@Component
@Order(value=1)
public class ServerRunner implements CommandLineRunner {
    @Resource
    public SocketIOServer socketIOServer;

    @Override
    public void run(String... args) throws Exception {
        socketIOServer.start();
        System.out.println("socket.io启动成功！");
    }
}
