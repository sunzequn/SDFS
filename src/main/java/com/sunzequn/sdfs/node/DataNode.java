package com.sunzequn.sdfs.node;

import com.sunzequn.sdfs.file.FileHandler;
import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.rmi.IRemote;
import com.sunzequn.sdfs.rmi.RemoteClient;
import com.sunzequn.sdfs.rmi.RemoteServer;
import com.sunzequn.sdfs.socket.client.SockClient;
import com.sunzequn.sdfs.socket.info.Ask4File;
import com.sunzequn.sdfs.socket.info.NodeUser;
import com.sunzequn.sdfs.socket.server.SockServer;
import com.sunzequn.sdfs.utils.FileUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
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
    private LinkedList<NodeInfo> deadNodes = new LinkedList<>();
    // 活跃结点上次的活跃时间,不包括leader
    private HashMap<String, Long> activeNodesLastTime = new HashMap<>();
    // 系统所有节点的用户数
    private List<NodeUser> nodeUsers = new ArrayList<>();
    // 自己节点的用户数据
    private NodeUser myUser = new NodeUser(0);
    private SockClient sockClient;
    private SockServer sockServer;
    private List<FileMeta> files = new ArrayList<>();
    //同步服务器文件的锁,防止重复同步
    private Set<String> fileSyncLock = new HashSet<>();
    // 写入远端文件的锁,保证文件的最终一致性
    private Set<String> remoteFileLock = new HashSet<>();
    private Map<String, String> remoteFileTimeMap = new HashMap<>();

    private RemoteServer remoteServer;
    private RemoteClient remoteClient;

    private String saveFolder;
    private FileHandler fileHandler;

    private Integer totolUserNum = 0;

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
            sockServer.start();
            DeadNodesFinderThread deadNodesFinderThread = new DeadNodesFinderThread(this);
            deadNodesFinderThread.start();
            ConnectionThread connectionThread = new ConnectionThread(this);
            connectionThread.start();
        }
        sockClient = new SockClient(this, leaderInfo.getIp(), leaderInfo.getPort(), selfInfo.getIp(), selfInfo.getPort(), selfInfo.getId());
        sockClient.start();
        if (isFirst) {
            // RMI端口是socket端口+1
            remoteServer = new RemoteServer(this, selfInfo.getPort() + 1);
            remoteClient = new RemoteClient(leaderInfo.getIp() + ":" + (leaderInfo.getPort() + 1));
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
    public void uploadFile(File file, byte[] contents) {
        try {
            FileMeta fileMeta = fileHandler.writeLocalFile(file, remoteClient.getTime(), contents);
            //同步到leader
            sockClient.sendFile(fileMeta);
            //列表更新
            fileMeta.setContents(null);
            files.add(fileMeta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateActiveNodes(LinkedList<NodeInfo> activeNodes) {
        if (isLeader)
            return;
        this.activeNodes = activeNodes;
    }

    @Override
    public NodeInfo getFreeNode() {
        //系统用户数自增
        totolUserNum += 1;
        for (NodeUser nodeUser : nodeUsers) {
            if (nodeUser.getNum() < 5) {
                return getNodeById(nodeUser.getNodeId());
            }
        }
        System.out.println(deadNodes);
        //启动新节点 >
        if (deadNodes.size() > 0) {
            NodeInfo nodeInfo = deadNodes.pop();
            System.out.println("启动新节点:" + nodeInfo.getId());
            RemoteClient remoteClient = new RemoteClient(nodeInfo.getIp() + ":" + (nodeInfo.getPort() + 1));
            remoteClient.start();
            return nodeInfo;
        }
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
        //节点用户数+1
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
    public void stop(NodeUser nodeUser) {
        if (nodeUser.getNodeId().equals(leaderInfo.getId())) return;
        System.out.println("暂时关闭节点:" + nodeUser.getNodeId());
        NodeInfo node = getNodeById(nodeUser.getNodeId());
        RemoteClient remoteClient = new RemoteClient(node.getIp() + ":" + (node.getPort() + 1));
        remoteClient.stop();
        Set<String> ids = new HashSet<>();
        ids.add(nodeUser.getNodeId());
        removeDeadNode(ids);
        deadNodes.add(node);
        nodeUsers.remove(nodeUser);
    }

    @Override
    public void restart() {
        sockClient.start();
    }

    @Override
    public Integer getTotalUserNum() {
        return totolUserNum;
    }

    @Override
    public void updateTotlaUserNun(Integer num) {
        if (isLeader) return;
        this.totolUserNum = num;
    }

    @Override
    public void removeDeadNode(Set<String> ids) {
        LinkedList<NodeInfo> nodeInfos = new LinkedList<>();
        for (NodeInfo activeNode : activeNodes) {
            if (!ids.contains(activeNode.getId())) {
                nodeInfos.add(activeNode);
            }
        }
        activeNodes = nodeInfos;
        for (String id : ids) {
            activeNodesLastTime.remove(id);
        }
    }

    @Override
    public byte[] downloadFile(String name) {
        try {
            return FileUtils.readFileToByteArray(new File(saveFolder + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<String> getActiveNodeIds() {
        Set<String> ids = new HashSet<>();
        for (NodeInfo activeNode : activeNodes) {
            ids.add(activeNode.getId());
        }
        return ids;
    }

    @Override
    public void writeRemoteFile(FileMeta fileMeta) {
        //有锁
        if (remoteFileLock.contains(fileMeta.getName())) {
            //比较新旧
            if (remoteFileTimeMap.get(fileMeta.getName()).compareTo(fileMeta.getTimestamp()) > 1) {
                //旧文件不写入
                return;
            }
        }
        System.out.println("写入远端文件");
        remoteFileLock.add(fileMeta.getName());
        remoteFileTimeMap.put(fileMeta.getName(), fileMeta.getTimestamp());
        fileHandler.writeRemoteFile(fileMeta);
        if (fileSyncLock.contains(fileMeta.getName())) {
            fileSyncLock.remove(fileMeta.getName());
        }
        fileMeta.setContents(null);
        files.add(fileMeta);
        remoteFileLock.remove(fileMeta.getName());
        remoteFileTimeMap.remove(fileMeta.getName());
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
        if (fileSyncLock.size() > 0)
            return;
        if (this.files.size() != leaderFiles.size()) {
            getNewFiles(this.files, leaderFiles);
            if (fileSyncLock.size() > 0) {
                System.out.println("////////////");
                System.out.println(fileSyncLock);
                for (String localName : fileSyncLock) {
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
            fileNames.add(oldFile.getName());
        }
        for (FileMeta newFile : newFiles) {
            if (!fileNames.contains(newFile.getName())) {
                fileSyncLock.add(newFile.getName());
            }
        }
    }

    private NodeInfo getNodeById(String id) {
        System.out.println(this.activeNodes);
        if (getLeaderNode().getId().equals(id))
            return getLeaderNode();
        for (NodeInfo activeNode : activeNodes) {
            if (activeNode.getId().equals(id))
                return activeNode;
        }
        return null;
    }

}
