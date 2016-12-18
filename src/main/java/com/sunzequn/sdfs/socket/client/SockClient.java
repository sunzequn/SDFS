package com.sunzequn.sdfs.socket.client;

import com.sunzequn.sdfs.node.IDataNodeAction;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Sloriac on 2016/12/16.
 *
 */
public class SockClient {

    private static final int DELAY = 500;

    // 用于回调
    private IDataNodeAction nodeAction;

    private String serverIp;
    private int ServerPort;

    private String clientIp;
    private int clientPort;

    private String id;
    private long lastTime;

    private Socket socket;

    public SockClient(IDataNodeAction nodeAction, String serverIp, int serverPort, String clientIp, int clientPort, String id) {
        this.nodeAction = nodeAction;
        this.serverIp = serverIp;
        ServerPort = serverPort;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.id = id;
    }

    public void start() {
        try {
            socket = new Socket(serverIp, ServerPort);
            lastTime = System.currentTimeMillis();
            new Thread(new KeepAliveHandler(lastTime, this)).start();
            new Thread(new ReceiveHandler(socket, this)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主节点故障
     */
    public void stop() {
        try {
            socket.close();
            nodeAction.updateLeader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(Object obj) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("连接中断");
            stop();
        }
    }

    public void sendAliveInfo() {
        KeepAlive keepAlive = new KeepAlive(nodeAction.getSelfNode(), nodeAction.getFilesInfo());
        sendInfo(keepAlive);
    }

    public <T> void receive(T t) {
        System.out.println("收到信息: " + t);
    }

    public String getId() {
        return id;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return ServerPort;
    }

    public String getClientIp() {
        return clientIp;
    }

    public int getClientPort() {
        return clientPort;
    }

    public static int getDELAY() {
        return DELAY;
    }
}
