package com.nanokylin.justforwardserver.controller;

import com.nanokylin.justforwardserver.common.Config;
import com.nanokylin.justforwardserver.service.WebSocketPoolService;
import com.nanokylin.justforwardserver.service.impl.WebSocketPoolServiceImpl;
import com.nanokylin.justforwardserver.utils.LogUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class WebSocketController extends WebSocketServer {
    private static final LogUtil log = new LogUtil();
    private static final WebSocketPoolService webSocketPoolService = new WebSocketPoolServiceImpl();

    public WebSocketController() {
    }

    public WebSocketController(InetSocketAddress address) {
        super(address);

    }

    public void initWebSocket(ThreadController threadController) {
        // 实例化WebSocket服务
        //WebSocketService webSocketService = new WebSocketServiceImpl();
        // 新建控制台线程
        Thread WebSocketThread = new WebSocketThread();
        threadController.getThreadPool().execute(WebSocketThread);
    }

    @Override
    public void onOpen(WebSocket connect, ClientHandshake handshake) {
        log.info("新连接: " + connect.getRemoteSocketAddress());
        this.userJoin(connect,connect.getResourceDescriptor());
    }

    @Override
    public void onClose(WebSocket connect, int code, String reason, boolean remote) {
        log.info("关闭: " + connect.getRemoteSocketAddress() + " 退出代码: " + code + " 地址信息: " + reason);
        this.userLeave(connect);
    }

    @Override
    public void onMessage(WebSocket connect, String message) {
        log.info("已收到来自主机的: " + connect.getRemoteSocketAddress() + ": " + message);
        webSocketPoolService.sendMessageToAll(message);
    }

    @Override
    public void onMessage(WebSocket connect, ByteBuffer message) {
        log.info("已收到ByteBuffer来自: " + connect.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket connect, Exception ex) {
        log.info("连接时发生错误: " + connect.getRemoteSocketAddress() + ":" + ex);
    }

    @Override
    public void onStart() {
        log.info("WebSocket服务成功启动！");
    }

    /**
     * 去除掉失效的websocket链接
     */
    public void userLeave(WebSocket connect) {
        webSocketPoolService.removeUser(connect);
    }

    /**
     * 将websocket加入用户池
     */
    public void userJoin(WebSocket connect, String userName) {
        webSocketPoolService.addUser(userName, connect);
    }
}

class WebSocketThread extends Thread {
    private static final LogUtil log = new LogUtil();

    @Override
    public void run() {
        super.setName("WebSocketThread");
        String host = (String) Config.getConfig("ip");
        int port = (int) Config.getConfig("port");
        WebSocketServer s = new WebSocketController(new InetSocketAddress(host, port));
        s.run();
    }
}
