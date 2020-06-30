package com.winnie.demo.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : winnie [wangliu023@qq.com]
 * @date : 2020/4/27
 * @desc
 */
@Configuration
public class SocketIOConfig {
    @Bean
    public SocketIOServer socketIOServer(){
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpKeepAlive(true);
        socketConfig.setSoLinger(0);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketConfig);
        // host在本地测试可以设置为localhost或者本机IP，在Linux服务器跑可换成服务器IP
        config.setHostname("192.168.100.35");
        config.setPort(8081);
        //设置最大每帧处理数据的长度，防止他人利用大数据来攻击服务器
        config.setMaxFramePayloadLength(1024 * 1024);
        config.setMaxHttpContentLength(1024 * 1024);
        //socket连接数大小（如只监听一个端口boss线程组为1即可）
        config.setBossThreads(1);
        config.setWorkerThreads(100);
        config.setAllowCustomRequests(true);
        //协议升级超时时间（毫秒），默认10秒。HTTP握手升级为ws协议超时时间
        config.setUpgradeTimeout(1000000);
        //Ping消息超时时间（毫秒），默认60秒，这个时间间隔内没有接收到心跳消息就会发送超时事件
        config.setPingTimeout(6000000);
        //Ping消息间隔（毫秒），默认25秒。客户端向服务器发送一条心跳消息间隔
        config.setPingInterval(25000);
        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}
