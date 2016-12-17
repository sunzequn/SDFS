package com.sunzequn.sdfs.socket.client;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sloriac on 2016/12/16.
 *
 */
public class KeepAlive implements Serializable {

    private static final long serialVersionUID = 1L;
    private long lastTime;
    private String clientIp;
    private int clientPort;
    private String id;

    public KeepAlive(String clientIp, int clientPort, String id) {
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.id = id;
        lastTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "KeepAlive{" +
                "lastTime=" + lastTime +
                ", clientIp='" + clientIp + '\'' +
                ", clientPort=" + clientPort +
                ", id='" + id + '\'' +
                '}';
    }
}