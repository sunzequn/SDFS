package com.sunzequn.sdfs.socket.info;

import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.node.NodeInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sloriac on 2016/12/16.
 *
 */
public class KeepAlive implements Serializable {

    private static final long serialVersionUID = 1L;
    private long lastTime;
    // 自己节点的相关信息
    private NodeInfo selfInfo;
    // 自己节点目前的文件
    private List<FileMeta> files;
    // 自己当前的用户数
    private NodeUser nodeUser;


    public KeepAlive(NodeInfo selfInfo, List<FileMeta> files, NodeUser nodeUser) {
        this.lastTime = System.currentTimeMillis();
        this.selfInfo = selfInfo;
        this.files = files;
        this.nodeUser = nodeUser;
    }

    public List<FileMeta> getFiles() {
        return files;
    }

    public NodeInfo getSelfInfo() {
        return selfInfo;
    }

    public NodeUser getNodeUser() {
        return nodeUser;
    }

    @Override
    public String toString() {
        return "KeepAlive{" +
                "lastTime=" + lastTime +
                ", selfInfo=" + selfInfo +
                ", files=" + files +
                ", nodeUser=" + nodeUser +
                '}';
    }
}