package com.sunzequn.sdfs.socket.server;

import com.sunzequn.sdfs.node.IDataNodeAction;
import com.sunzequn.sdfs.node.NodeInfo;
import com.sunzequn.sdfs.socket.client.KeepAlive;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by Sloriac on 2016/12/16.
 *
 */
public class SockServer {
    // 用于回调
    private IDataNodeAction nodeAction;
    private int port;
    private Set<String> clientIds = new HashSet<String>();
    private Map<String, Socket> socketMap = new HashMap<>();
    protected ServerSocket serverSocket;

    public SockServer(IDataNodeAction nodeAction, int port) {
        this.nodeAction = nodeAction;
        this.port = port;
    }

    public void start(){
        try {
            serverSocket = new ServerSocket(port);
            while(true){
                Socket socket  = serverSocket.accept();
                new Thread(new SockServerHandler(socket, this)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleHeart(KeepAlive keepAlive, Socket socket) {
        String clientId = keepAlive.getSelfInfo().getId();
        if (!clientIds.contains(clientId)) {
            clientIds.add(clientId);
            socketMap.put(clientId, socket);
            nodeAction.getActiveNodesInfo().add(keepAlive.getSelfInfo());
        }
        nodeAction.updateFiles(keepAlive.getFiles());
    }
}
