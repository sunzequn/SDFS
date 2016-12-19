package com.sunzequn.sdfs.node;

import com.sunzequn.sdfs.file.FileHandler;
import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.rmi.RemoteServer;
import com.sunzequn.sdfs.socket.client.SockClient;
import com.sunzequn.sdfs.socket.info.Ask4File;
import com.sunzequn.sdfs.socket.info.NodeUser;
import com.sunzequn.sdfs.socket.server.ServerThread;
import com.sunzequn.sdfs.socket.server.SockServer;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by Sloriac on 2016/12/18.
 * 数据服务节点
 */
public class DataNode implements IDataNodeAction {

    // 自己节点的相关信息
    private NodeInfo selfInfo;
    // 是否是leader节点
    private boolean isLeader = false;
    // leader节点的相关信息
    private NodeInfo leaderInfo;
    // 系统中活跃的其他兄弟节点,不包括leader
    private LinkedList<NodeInfo> activeNodes = new LinkedList<>();
    private List<NodeInfo> deadNodes = new ArrayList<>();
    // 活跃结点上次的活跃时间,不包括leader
    private HashMap<String, Long> activeNodesLastTime = new HashMap<>();
    // 系统所有节点的用户数
    private List<NodeUser> nodeUsers = new ArrayList<>();
    // 自己节点的用户数据
    private NodeUser myUser = new NodeUser(0);
    private SockClient sockClient;
    private SockServer sockServer;
    private List<FileMeta> files = new ArrayList<>();
    private Set<String> fileLocalNamesSyncLock = new HashSet<>();
    private RemoteServer remoteServer;

    private String saveFolder;
    private FileHandler fileHandler;

    public DataNode(NodeInfo selfInfo, NodeInfo leaderInfo, String saveFolder) {
        this.selfInfo = selfInfo;
        this.leaderInfo = leaderInfo;
        this.saveFolder = saveFolder;
        fileHandler = new FileHandler(saveFolder, selfInfo.getId());
    }

    public DataNode(NodeInfo selfInfo, boolean isLeader, String saveFolder) {
        this.selfInfo = selfInfo;
        this.isLeader = isLeader;
        if (isLeader) {
            this.leaderInfo = selfInfo;
        }
        this.saveFolder = saveFolder;
        fileHandler = new FileHandler(saveFolder, selfInfo.getId());
    }

    public void start(boolean isFirst) {
        // 异步开启leader端
        if (isLeader) {
            sockServer = new SockServer(this, selfInfo.getPort());
            System.out.println("leader启动socket服务" + selfInfo.getPort());
            sockServer.start();
        }
        sockClient = new SockClient(this, leaderInfo.getIp(), leaderInfo.getPort(), selfInfo.getIp(), selfInfo.getPort(), selfInfo.getId());
        sockClient.start();
        if (isFirst) {
            // RMI端口是socket端口+1
            remoteServer = new RemoteServer(this, selfInfo.getPort() + 1);
            myUser.setNodeId(selfInfo.getId());
        }

    }

    @Override
    public void writeLocalFile(File file) {
        System.out.println("写入本地文件");
        // 写入本地
        FileMeta fileMeta = fileHandler.writeLocalFile(file);
        //同步到leader
        sockClient.sendFile(fileMeta);
        //列表更新
        fileMeta.setContents(null);
        files.add(fileMeta);
    }

    @Override
    public void updateActiveNodes(LinkedList<NodeInfo> activeNodes) {
        if (isLeader)
            return;
        this.activeNodes = activeNodes;
    }

    @Override
    public NodeInfo getFreeNode() {

        for (NodeUser nodeUser : nodeUsers) {
            if (nodeUser.getNum() < 5) {
                return getNodeById(nodeUser.getNodeId());
            }
        }
        //启动新节点
        return null;
    }

    @Override
    public List<NodeUser> getNodeUsers() {
        return nodeUsers;
    }

    @Override
    public void updateNodeUsers(List<NodeUser> nodeUsers) {
        if (isLeader)
            return;
        this.nodeUsers = nodeUsers;
    }

    @Override
    public void updateNodeUser(NodeUser nodeUser) {
        for (NodeUser user : nodeUsers) {
            if (user.getNodeId().equals(nodeUser.getNodeId())) {
                user.setNum(nodeUser.getNum());
                return;
            }
        }
        nodeUsers.add(nodeUser);
    }

    @Override
    public NodeUser getMyUser() {
        return myUser;
    }

    @Override
    public void addUser() {
        myUser.setNum(myUser.getNum() + 1);
    }

    @Override
    public void removeUser() {
        myUser.setNum(myUser.getNum() - 1);
    }

    @Override
    public void updateActiveNodesLastTime(HashMap<String, Long> activeNodesLastTime) {
        if (isLeader) return;
        this.activeNodesLastTime = activeNodesLastTime;
    }

    @Override
    public void updateActiveNodesLastTime(String id, Long time) {
        if (isLeader) return;
        activeNodesLastTime.put(id, time);
    }

    @Override
    public HashMap<String, Long> getActiveNodesLastTime() {
        return activeNodesLastTime;
    }

    /**
     * 关闭socket连接,但是服务还在
     * 不选举新leader
     */
    @Override
    public void stop() {
        sockClient.stop(false);
    }


    @Override
    public void restart() {
        start(false);
    }

    @Override
    public void writeRemoteFile(FileMeta fileMeta) {
        System.out.println("写入远端文件");
        fileHandler.writeRemoteFile(fileMeta);
        if (fileLocalNamesSyncLock.contains(fileMeta.getLocalName())) {
            fileLocalNamesSyncLock.remove(fileMeta.getLocalName());
        }
        fileMeta.setContents(null);
        files.add(fileMeta);
    }

    @Override
    public FileMeta popuFile(FileMeta fileMeta) {
        return fileHandler.popuFile(fileMeta);
    }

    @Override
    public void updateLeader() {
        if (activeNodes.size() == 0) return;
        NodeInfo pleader = leaderInfo;
        leaderInfo = activeNodes.pop();
        long sleepTime = 1000;
        if (leaderInfo.getId().equals(selfInfo.getId())) {
            //过滤掉nodeusers里面的原先leader节点
            nodeUsers = resetNodeUsers(pleader);
            isLeader = true;
            sleepTime = 10;
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        start(false);
        System.out.println("选举新的leader节点：" + leaderInfo);
    }

    private List<NodeUser> resetNodeUsers(NodeInfo pleader) {
        List<NodeUser> newNodeUsers = new ArrayList<>();
        for (NodeUser nodeUser : nodeUsers) {
            if (!nodeUser.getNodeId().equals(pleader.getId())) {
                newNodeUsers.add(nodeUser);
            }
        }
        return newNodeUsers;
    }

    @Override
    public void updateFromLeaderFiles(List<FileMeta> leaderFiles) {
//        System.out.println("本地" + files);
//        System.out.println("leader" + leaderFiles);
        //检查锁，还有在同步的文件
        if (fileLocalNamesSyncLock.size() > 0)
            return;
        if (this.files.size() != leaderFiles.size()) {
            getNewFiles(this.files, leaderFiles);
            if (fileLocalNamesSyncLock.size() > 0) {
                System.out.println("////////////");
                System.out.println(fileLocalNamesSyncLock);
                for (String localName : fileLocalNamesSyncLock) {
                    //请求填充file信息
                    Ask4File ask4File = new Ask4File(selfInfo.getId(), localName);
                    sockClient.sendInfo(ask4File);
                }
            }
        }
    }

    @Override
    public NodeInfo getSelfNode() {
        return this.selfInfo;
    }

    @Override
    public void updateActiveNode(NodeInfo activeNode) {
        this.activeNodes.add(activeNode);
    }

    @Override
    public LinkedList<NodeInfo> getActiveNodesInfo() {
        return this.activeNodes;
    }

    @Override
    public NodeInfo getLeaderNode() {
        return leaderInfo;
    }


    @Override
    public List<FileMeta> getFilesInfo() {
        return this.files;
    }

    private void getNewFiles(List<FileMeta> oldFiles, List<FileMeta> newFiles) {
        Set<String> fileNames = new HashSet<>();
        for (FileMeta oldFile : oldFiles) {
            fileNames.add(oldFile.getLocalName());
        }
        for (FileMeta newFile : newFiles) {
            if (!fileNames.contains(newFile.getLocalName())) {
                fileLocalNamesSyncLock.add(newFile.getLocalName());
            }
        }
    }

    private NodeInfo getNodeById(String id) {
        if (getLeaderNode().getId().equals(id))
            return getLeaderNode();
        for (NodeInfo activeNode : activeNodes) {
            if (activeNode.getId().equals(id))
                return activeNode;
        }
        return null;
    }


}
