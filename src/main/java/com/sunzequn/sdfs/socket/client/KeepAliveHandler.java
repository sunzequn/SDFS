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
        while (true) {
            try {
                if (System.currentTimeMillis() - lastTime > keepAliveDelay) {
                    client.sendInfo(new KeepAlive(client.getClientIp(), client.getClientPort(), client.getId()));
                    lastTime = System.currentTimeMillis();
                } else {
                    Thread.sleep(checkDelay);
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
