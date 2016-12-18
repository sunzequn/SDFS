package com.sunzequn.sdfs.socket.client;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by Sloriac on 2016/12/16.
 */
public class ReceiveHandler extends Thread {

    private Socket socket;
    private SockClient client;

    public ReceiveHandler(Socket socket, SockClient client) {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (socket.isClosed())
                    return;
                InputStream in = socket.getInputStream();

                if (in.available() > 0) {
                    ObjectInputStream ois = new ObjectInputStream(in);
                    Object obj = ois.readObject();
                    client.receive(obj);
                } else {
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                e.printStackTrace();
                stop();
            }
        }
    }
}
