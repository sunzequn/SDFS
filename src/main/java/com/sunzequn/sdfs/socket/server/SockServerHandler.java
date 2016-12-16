package com.sunzequn.sdfs.socket.server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by Sloriac on 2016/12/16.
 */
public class SockServerHandler implements Runnable{

    private Socket socket;

    public SockServerHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        while (true) {
            InputStream in = null;
            try {
                in = socket.getInputStream();
                if (in.available() > 0) {
                    ObjectInputStream ois = new ObjectInputStream(in);
                    Object obj = ois.readObject();
                    System.out.println(obj.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}