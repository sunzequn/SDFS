package com.sunzequn.sdfs.socket.server;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class ServerThread extends Thread {

    private SockServer sockServer;

    public ServerThread(SockServer sockServer) {
        this.sockServer = sockServer;
    }

    @Override
    public void run() {
        sockServer.start();
    }
}
