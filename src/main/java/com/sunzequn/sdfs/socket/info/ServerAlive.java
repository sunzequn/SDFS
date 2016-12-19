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
    private List<FileMeta> files;
    private List<NodeUser> nodeUsers;

    public ServerAlive(LinkedList<NodeInfo> activeNodes, List<FileMeta> files, List<NodeUser> nodeUsers) {
        this.activeNodes = activeNodes;
        this.files = files;
        this.nodeUsers = nodeUsers;
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

    @Override
    public String toString() {
        return "ServerAlive{" +
                "activeNodes=" + activeNodes +
                ", files=" + files +
                ", nodeUsers=" + nodeUsers +
                '}';
    }
}
