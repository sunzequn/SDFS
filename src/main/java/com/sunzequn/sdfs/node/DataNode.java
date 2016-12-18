package com.sunzequn.sdfs.node;

import com.sunzequn.sdfs.file.FileHandler;
import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.socket.client.SockClient;
import com.sunzequn.sdfs.socket.info.Ask4File;
import com.sunzequn.sdfs.socket.server.ServerThread;
import com.sunzequn.sdfs.socket.server.SockServer;

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
    // 系统中活跃的其他兄弟节点
    private LinkedList<NodeInfo> activeNodes = new LinkedList<>();
    private SockClient sockClient;
    private SockServer sockServer;
    private List<FileMeta> files = new ArrayList<>();
    private Set<String> fileLocalNamesSyncLock = new HashSet<>();

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
    public void writeLocalFile(String path) {
        System.out.println("写入本地文件");
        // 写入本地
        FileMeta fileMeta = fileHandler.writeLocalFile(path);
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
        leaderInfo = activeNodes.pop();
        long sleepTime = 1000;
        if (leaderInfo.getId().equals(selfInfo.getId())) {
            isLeader = true;
            sleepTime = 10;
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        start();
        System.out.println("选举新的leader节点：" + leaderInfo);
    }

    @Override
    public void updateFromLeaderFiles(List<FileMeta> leaderFiles) {
        System.out.println("本地" + files);
        System.out.println("leader" + leaderFiles);
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

}
