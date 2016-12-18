package com.sunzequn.sdfs.socket.client;

/**
 * Created by Sloriac on 2016/12/16.
 */
public class KeepAliveHandler extends Thread {

    private long lastTime;
    private SockClient client;

    public KeepAliveHandler(long lastTime, SockClient client) {
        this.lastTime = lastTime;
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (System.currentTimeMillis() - lastTime > SockClient.getDELAY()) {
                    client.sendAliveInfo();
                    lastTime = System.currentTimeMillis();
                } else {
                    Thread.sleep(SockClient.getDELAY());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
