package com.sunzequn.sdfs.socket.server;

import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.node.NodeInfo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class ServerAlive implements Serializable {

    private static final long serialVersionUID = 1L;

    private LinkedList<NodeInfo> activeNodes;
    private List<FileMeta> files;

    public ServerAlive(LinkedList<NodeInfo> activeNodes, List<FileMeta> files) {
        this.activeNodes = activeNodes;
        this.files = files;
    }

    public LinkedList<NodeInfo> getActiveNodes() {
        return activeNodes;
    }

    public List<FileMeta> getFiles() {
        return files;
    }


    @Override
    public String toString() {
        return "ServerAlive{" +
                "activeNodes=" + activeNodes +
                ", files=" + files +
                '}';
    }
}
