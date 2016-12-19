package com.sunzequn.sdfs.socket.info;

import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.node.NodeInfo;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class ServerAlive implements Serializable {

    private static final long serialVersionUID = 1L;

    private LinkedList<NodeInfo> activeNodes;
    private HashMap<String, Long> activeNodesLastTime;
    private List<FileMeta> files;
    private List<NodeUser> nodeUsers;
    private Integer totalUserNum;

    public ServerAlive(LinkedList<NodeInfo> activeNodes, List<FileMeta> files, List<NodeUser> nodeUsers, HashMap<String, Long> activeNodesLastTime, Integer totalUserNum) {
        this.activeNodes = activeNodes;
        this.files = files;
        this.nodeUsers = nodeUsers;
        this.activeNodesLastTime = activeNodesLastTime;
        this.totalUserNum = totalUserNum;
    }

    public LinkedList<NodeInfo> getActiveNodes() {
        return activeNodes;
    }

    public List<FileMeta> getFiles() {
        return files;
    }

    public List<NodeUser> getNodeUsers() {
        return nodeUsers;
    }

    public HashMap<String, Long> getActiveNodesLastTime() {
        return activeNodesLastTime;
    }

    public Integer getTotalUserNum() {
        return totalUserNum;
    }

    @Override
    public String toString() {
        return "ServerAlive{" +
                "activeNodes=" + activeNodes +
                ", activeNodesLastTime=" + activeNodesLastTime +
                ", files=" + files +
                ", nodeUsers=" + nodeUsers +
                ", totalUserNum=" + totalUserNum +
                '}';
    }
}
