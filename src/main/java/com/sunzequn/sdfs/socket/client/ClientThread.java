package com.sunzequn.sdfs.socket.client;

import com.sunzequn.sdfs.socket.server.SockServer;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class ClientThread extends Thread {

    private SockServer sockServer;

    public ClientThread(SockServer sockServer) {
        this.sockServer = sockServer;
    }

    @Override
    public void run() {
        sockServer.start();
    }
}
