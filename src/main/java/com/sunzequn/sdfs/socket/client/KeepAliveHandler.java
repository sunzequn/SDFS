package com.sunzequn.sdfs.socket.client;

/**
 * Created by Sloriac on 2016/12/16.
 */
public class KeepAliveHandler implements Runnable {

    private long lastTime;
    private SockClient client;

    public KeepAliveHandler(long lastTime, SockClient client) {
        this.lastTime = lastTime;
        this.client = client;
    }

    public void run() {
        long checkDelay = 1000;
        long keepAliveDelay = 2000;
        try {
            while (true){
                if (System.currentTimeMillis() - lastTime > keepAliveDelay) {
                    client.sendInfo(new KeepAlive());
                    lastTime = System.currentTimeMillis();
                } else {
                    Thread.sleep(checkDelay);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            client.stop();
            return;
        }
    }
}
