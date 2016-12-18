package com.sunzequn.sdfs.node;

import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.socket.client.SockClient;
import com.sunzequn.sdfs.socket.server.ServerThread;
import com.sunzequn.sdfs.socket.server.SockServer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sloriac on 2016/12/18.
 * 数据服务节点
 */
public class DataNode implements IDataNodeAction {

    // 自己节点的相关信息
    private NodeInfo selfInfo;
    // leader节点的相关信息
    private NodeInfo leaderInfo;
    // 系统中活跃的其他兄弟节点
    private LinkedList<NodeInfo> activeNodes = new LinkedList<>();
    private SockClient sockClient;
    private SockServer sockServer;
    private List<FileMeta> files = new ArrayList<>();
    private boolean isLeader = false;
    private LeaderHandler leaderHandler = new LeaderHandler(selfInfo);

    public DataNode(NodeInfo selfInfo, NodeInfo leaderInfo) {
        this.selfInfo = selfInfo;
        this.leaderInfo = leaderInfo;
    }

    public DataNode(NodeInfo selfInfo, boolean isLeader) {
        this.selfInfo = selfInfo;
        this.isLeader = isLeader;
        if (isLeader) {
            this.leaderInfo = selfInfo;
        }
    }

    public void start() {
        // 异步开启leader端
        if (isLeader) {
            sockServer = new SockServer(this, selfInfo.getPort());
            ServerThread serverThread = new ServerThread(sockServer);
            serverThread.start();
        }
        sockClient = new SockClient(this, leaderInfo.getIp(), leaderInfo.getPort(), selfInfo.getIp(), selfInfo.getPort(), selfInfo.getId());
        sockClient.start();
    }

    @Override
    public void updateActiveNodes(LinkedList<NodeInfo> activeNodes) {
        this.activeNodes = activeNodes;
    }

    @Override
    public LinkedList<NodeInfo> getActiveNodesInfo() {
        return this.activeNodes;
    }

    @Override
    public void updateFiles(List<FileMeta> files) {
//        if this.files.size() == files.size()
        this.files = files;
    }

    @Override
    public List<FileMeta> getFilesInfo() {
        return this.files;
    }

    @Override
    public void updateLeader() {
        leaderInfo = activeNodes.pop();
        start();
        System.out.println("启动新的leader节点：" + leaderInfo);
    }

    @Override
    public NodeInfo getSelfNode() {
        return this.selfInfo;
    }
}
