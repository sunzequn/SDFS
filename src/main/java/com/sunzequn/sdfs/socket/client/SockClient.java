package com.sunzequn.sdfs.socket.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Sloriac on 2016/12/16.
 */
public class SockClient {

    private String serverIp;
    private int port;
    private Socket socket;
    private long lastTime;

    public SockClient(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public void start() {
        try {
            socket = new Socket(serverIp, port);
            lastTime = System.currentTimeMillis();
            new Thread(new KeepAliveHandler(lastTime, this)).start();
            new Thread(new ReceiveHandler(socket, this)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            socket.close();
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
        }
    }

    public <T> void receive(T t) {
        System.out.println("收到信息: " + t);
    }


    public static void main(String[] args) {
        SockClient sockClient = new SockClient("localhost", 1111);
        sockClient.start();
    }
}
