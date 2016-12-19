package com.sunzequn.sdfs.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Sloriac on 2016/12/18.
 *
 */
public class ServerThread extends Thread {

    private SockServer sockServer;
    private ServerSocket serverSocket;

    public ServerThread(SockServer sockServer, ServerSocket serverSocket) {
        this.sockServer = sockServer;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(new SockServerHandler(socket, sockServer)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
