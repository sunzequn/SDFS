package com.sunzequn.sdfs.node;

import java.io.Serializable;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class NodeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String ip;
    private int port;

    public NodeInfo(String id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
