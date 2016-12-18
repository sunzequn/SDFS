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
                // 主节点出现问题, 连接中断
                client.stop();
                //
            }
        }
    }
}
