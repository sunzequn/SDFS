package com.sunzequn.sdfs.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sloriac on 2016/12/16.
 *
 */
public class SockServer {

    private int port;
    private Set<String> clientIds = new HashSet<String>();
    protected ServerSocket serverSocket;

    public SockServer(int port) {
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

    public static void main(String[] args) {
        SockServer sockServer = new SockServer(1111);
        sockServer.start();
    }
}
