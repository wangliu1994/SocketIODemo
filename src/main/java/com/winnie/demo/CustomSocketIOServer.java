package com.winnie.demo;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author : winnie [wangliu023@qq.com]
 * @date : 2020/4/27
 * @desc 参考博客--https://blog.csdn.net/weixin_33937778/article/details/92158492
 */
@Component
@Slf4j
public class CustomSocketIOServer {

    @Resource
    public SocketIOServer socketIOServer;

    /**
     * 在WebSocket测试网页：http://www.websocket-test.com/
     * 输入以下格式地址链接：ws://192.168.100.35:8081/socket.io/?EIO=3&transport=websocket
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        String sa = client.getRemoteAddress().toString();
        String clientIp = sa.substring(1, sa.indexOf(":"));// 获取设备ip
        log.info(clientIp + "-------------------------" + "客户端已连接," + "clientId = " + client.getSessionId());
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        log.info("客户端参数：{}", params);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String sa = client.getRemoteAddress().toString();
        String clientIp = sa.substring(1, sa.indexOf(":"));// 获取设备ip
        log.info(clientIp + "-------------------------" + "客户端已断开连接," + "clientId = " + client.getSessionId());
    }


    /**
     * 在WebSocket测试网页：http://www.websocket-test.com/
     * 输入以下格式内容：42["text","data"]
     * <p>
     * 对应js的：socket.emit('text','data');
     * 如果js使用：socket.on('text',fn)，则可以收到client反馈的消息
     */
    @OnEvent(value = "text")
    public void onEvent(SocketIOClient client, AckRequest ackRequest, String data) {
        client.joinRoom("text");
        log.info("订阅text频道 {}", client.getAllRooms());

        // 客户端推送advert_info事件时，onData接受数据，这里是string类型的json数据，还可以为Byte[],object其他类型
        String sa = client.getRemoteAddress().toString();
        // 获取客户端连接的ip
        String clientIp = sa.substring(1, sa.indexOf(":"));
        // 获取客户端url参数
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        log.info(clientIp + "：客户端：************" + data);
      /*  可以传入Json串
        JSONObject gpsData = (JSONObject) JSONObject.parse(data);
        String userIds = gpsData.get("userName") + "";
        String taskIds = gpsData.get("password") + "";*/
        client.sendEvent("text", "后台得到了数据" + data);
    }

    /**
     * 订阅频道
     * 42["testRoom1On","data"]
     * socket.emit('testRoom1On','data');
     * socket.on('testRoom1On',fn)
     */
    @OnEvent(value = "testRoom1On")
    public void onTestRoom1OnEvent(SocketIOClient client, AckRequest ackRequest, String data) {
        client.joinRoom("room1");
        log.info("订阅test-room1频道 {}", client.getAllRooms());
        client.sendEvent("testRoom1On", "后台得到了数据" + data);
    }

    @OnEvent(value = "testRoom2On")
    public void onTestRoom2OnEvent(SocketIOClient client, AckRequest ackRequest, String data) {
        client.joinRoom("room2");
        log.info("订阅test-room2频道 {}", client.getAllRooms());
        client.sendEvent("testRoom2On", "后台得到了数据" + data);
    }

    /**
     * 解除订阅频道
     * 42["testRoom1Off","data"]
     * <p>
     * socket.emit('testRoom1Off','data');
     * socket.on('testRoom1Off',fn)
     */
    @OnEvent(value = "testRoom1Off")
    public void onTestRoom1OffEvent(SocketIOClient client, AckRequest ackRequest, String data) {
        client.leaveRoom("room1");
        log.info("解除订阅test-room1频道 {}", client.getAllRooms());
        client.sendEvent("testRoom1Off", "后台得到了数据" + data);
    }

    @OnEvent(value = "testRoom2Off")
    public void onTestRoom2OffEvent(SocketIOClient client, AckRequest ackRequest, String data) {
        client.leaveRoom("room2");
        log.info("解除订阅test-room2频道 {}", client.getAllRooms());
        client.sendEvent("testRoom2Off", "后台得到了数据" + data);
    }

    @OnEvent(value = "rooms")
    public void rooms(SocketIOClient client, AckRequest ackRequest, String data) {
        log.info("所有频道{}", client.getAllRooms());
    }

    /**
     * 给单人发送消息
     * 42["testSendClient","data"]
     * <p>
     * socket.emit('testSendClient','data');
     * socket.on('testSendClient',fn)
     */
    @OnEvent(value = "testSendClient")
    public void onTestSendClient1Event(SocketIOClient client, AckRequest ackRequest, String data) {
        log.info("后台主动发送数据");
        socketIOServer.getClient(client.getSessionId()).sendEvent("后台主动发送数据", data);
    }


    /**
     * 给指定房间（频道）发送消息
     * 42["testSendRoom1","data"]
     * <p>
     * socket.emit('testSendRoom1','data');
     * socket.on('testSendRoom1',fn)
     */
    @OnEvent(value = "testSendRoom1")
    public void onTestSendRoom1Event(SocketIOClient client, AckRequest ackRequest, String data) {
        log.info("后台主动发送数据");
        socketIOServer.getRoomOperations("room1").sendEvent("后台主动发送数据", data);
    }

    @OnEvent(value = "testSendRoom2")
    public void onTestSendRoom2Event(SocketIOClient client, AckRequest ackRequest, String data) {
        log.info("后台主动发送数据");
        socketIOServer.getRoomOperations("room2").sendEvent("后台主动发送数据", data);
    }
}
