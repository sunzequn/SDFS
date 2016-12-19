package com.sunzequn.sdfs.socket.info;

import java.io.Serializable;

/**
 * Created by sloriac on 16-12-19.
 */
public class NodeUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nodeId;
    private int num;

    public NodeUser(int num) {
        this.num = num;
    }

    public NodeUser(String nodeId, int num) {
        this.nodeId = nodeId;
        this.num = num;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "NodeUser{" +
                "nodeId='" + nodeId + '\'' +
                ", num=" + num +
                '}';
    }
}
