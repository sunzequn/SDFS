package com.sunzequn.sdfs.socket.server;

import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.node.IDataNodeAction;
import com.sunzequn.sdfs.node.NodeInfo;
import com.sunzequn.sdfs.socket.info.Ask4File;
import com.sunzequn.sdfs.socket.info.KeepAlive;
import com.sunzequn.sdfs.socket.info.ServerAlive;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by Sloriac on 2016/12/16.
 */
public class SockServer {

    private static final int DELAY = 500;
    // 用于回调
    private IDataNodeAction nodeAction;
    private int port;
    private Map<String, Socket> socketMap = new HashMap<>();
    protected ServerSocket serverSocket;

    public SockServer(IDataNodeAction nodeAction, int port) {
        this.nodeAction = nodeAction;
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            ServerThread serverThread = new ServerThread(this, serverSocket);
            serverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(Object obj, Socket socket) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("连接中断");
        }
    }

    public void handleHeart(KeepAlive keepAlive, Socket socket) {
        System.out.println("客户端心跳: " + keepAlive);
        // 每一次心跳更新一次
        nodeAction.updateActiveNodesLastTime(keepAlive.getSelfInfo().getId(), System.currentTimeMillis());
        String clientId = keepAlive.getSelfInfo().getId();
        // ids保存左右节点
        Set<String> ids = nodeAction.getActiveNodeIds();
        if (!ids.contains(clientId)) {
            ids.add(clientId);
            socketMap.put(clientId, socket);
            // ActiveNodes保存除了leader之外的节点
            if (!clientId.equals(nodeAction.getLeaderNode().getId()))
                nodeAction.updateActiveNode(keepAlive.getSelfInfo());

        }
        nodeAction.updateNodeUser(keepAlive.getNodeUser());
        sendInfo(new ServerAlive(nodeAction.getActiveNodesInfo(), nodeAction.getFilesInfo(),
                nodeAction.getNodeUsers(), nodeAction.getActiveNodesLastTime(), nodeAction.getTotalUserNum()), socket);
    }


    public void handleNewFile(FileMeta fileMeta) {
        Set<String> ids = nodeAction.getActiveNodeIds();
        // 转发给其他节点
        System.out.println("leader 收到新文件：" + fileMeta.getName() + ", 转发给" + ids);
        for (String id : ids) {
            if (!id.equals(fileMeta.getSrcNode())) {
                sendInfo(fileMeta, socketMap.get(id));
            }
        }
    }

    public void downloadFile(Ask4File ask4File) {
        for (FileMeta fileMeta : nodeAction.getFilesInfo()) {
            if (fileMeta.getName().equals(ask4File.getLocalName())) {
                fileMeta = nodeAction.popuFile(fileMeta);
                sendInfo(fileMeta, socketMap.get(ask4File.getId()));
            }
        }
    }
}
