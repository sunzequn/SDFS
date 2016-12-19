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
    Set<String> ids = new HashSet<>();
    private Map<String, Socket> socketMap = new HashMap<>();
    protected ServerSocket serverSocket;

    public SockServer(IDataNodeAction nodeAction, int port) {
        this.nodeAction = nodeAction;
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(serverSocket);
            for (NodeInfo nodeInfo : nodeAction.getActiveNodesInfo()) {
                ids.add(nodeInfo.getId());
            }
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
        String clientId = keepAlive.getSelfInfo().getId();
        // ids保存左右节点
        if (!ids.contains(clientId)) {
            ids.add(clientId);
            socketMap.put(clientId, socket);
            // ActiveNodes保存除了leader之外的节点
            if (!clientId.equals(nodeAction.getLeaderNode().getId()))
                nodeAction.getActiveNodesInfo().add(keepAlive.getSelfInfo());
        }
        nodeAction.updateNodeUser(keepAlive.getNodeUser());
        sendInfo(new ServerAlive(nodeAction.getActiveNodesInfo(), nodeAction.getFilesInfo(), nodeAction.getNodeUsers()), socket);
    }


    public void handleNewFile(FileMeta fileMeta) {
        // 转发给其他节点
        System.out.println("leader 收到新文件：" + fileMeta.getName() + ", 转发给" + ids);
        System.out.println(fileMeta);
        for (String id : ids) {
            if (!id.equals(fileMeta.getSrcNode())) {
                sendInfo(fileMeta, socketMap.get(id));
            }
        }
    }

    public void downloadFile(Ask4File ask4File) {
        for (FileMeta fileMeta : nodeAction.getFilesInfo()) {
            if (fileMeta.getLocalName().equals(ask4File.getLocalName())) {
                fileMeta = nodeAction.popuFile(fileMeta);
                sendInfo(fileMeta, socketMap.get(ask4File.getId()));
            }
        }
    }
}
