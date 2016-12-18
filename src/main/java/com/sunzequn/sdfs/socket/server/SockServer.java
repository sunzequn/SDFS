package com.sunzequn.sdfs.socket.server;

import com.sunzequn.sdfs.node.IDataNodeAction;
import com.sunzequn.sdfs.node.NodeInfo;
import com.sunzequn.sdfs.socket.client.KeepAlive;
import com.sunzequn.sdfs.socket.client.SockClient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by Sloriac on 2016/12/16.
 *
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

    public void start(){
        try {
            serverSocket = new ServerSocket(port);
            for (NodeInfo nodeInfo : nodeAction.getActiveNodesInfo()) {
                ids.add(nodeInfo.getId());
            }
            while(true){
                Socket socket  = serverSocket.accept();
                new Thread(new SockServerHandler(socket, this)).start();
            }
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
        String clientId = keepAlive.getSelfInfo().getId();
        if (!ids.contains(clientId) && !clientId.equals(nodeAction.getLeaderNode().getId())) {
            ids.add(clientId);
            socketMap.put(clientId, socket);
            nodeAction.getActiveNodesInfo().add(keepAlive.getSelfInfo());
        }
        nodeAction.updateFiles(keepAlive.getFiles());
        sendInfo(new ServerAlive(nodeAction.getActiveNodesInfo(), nodeAction.getFilesInfo()), socket);
    }
}
