package com.sunzequn.sdfs.socket.client;

import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.node.IDataNodeAction;
import com.sunzequn.sdfs.socket.info.KeepAlive;
import com.sunzequn.sdfs.socket.info.ServerAlive;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Sloriac on 2016/12/16.
 *
 */
public class SockClient {

    private static final int DELAY = 1000;

    // 用于回调
    private IDataNodeAction nodeAction;

    private String serverIp;
    private int serverPort;

    private String clientIp;
    private int clientPort;

    private String id;
    private long lastTime;

    private Socket socket;

    private KeepAliveHandler keepAliveHandler;
    private ReceiveHandler receiveHandler;

    public SockClient(IDataNodeAction nodeAction, String serverIp, int serverPort, String clientIp, int clientPort, String id) {
        this.nodeAction = nodeAction;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.id = id;
    }

    public void start() {
        try {
            System.out.println(serverIp + serverPort);
            socket = new Socket(serverIp, serverPort);
            lastTime = System.currentTimeMillis();
            keepAliveHandler = new KeepAliveHandler(lastTime, this);
            keepAliveHandler.start();
            receiveHandler = new ReceiveHandler(socket, this);
            receiveHandler.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主节点故障
     */
    public void stop() {
        System.out.println("--连接中断--");
        try {
            socket.close();
            nodeAction.updateLeader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(FileMeta fileMeta) {
        sendInfo(fileMeta);
    }

    public void sendInfo(Object obj) {
        try {
            if (socket.isClosed())
                return;
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }
    }

    public void sendAliveInfo() {
        KeepAlive keepAlive = new KeepAlive(nodeAction.getSelfNode(), nodeAction.getFilesInfo(), nodeAction.getMyUser());
        sendInfo(keepAlive);
    }

    public <T> void receive(T t) {
        System.out.println("收到信息: " + t);
        if (t instanceof ServerAlive) {
            nodeAction.updateActiveNodes(((ServerAlive) t).getActiveNodes());
            nodeAction.updateNodeUsers(((ServerAlive) t).getNodeUsers());
            nodeAction.updateFromLeaderFiles(((ServerAlive) t).getFiles());
        }
        // leader发来新文件
        else if (t instanceof FileMeta) {
            System.out.println("节点" + id + " 收到文件" + ((FileMeta) t).getName());
            nodeAction.writeRemoteFile((FileMeta) t);
        }
    }

    public String getId() {
        return id;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return serverPort;
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
