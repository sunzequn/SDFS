package com.sunzequn.sdfs.node;

import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.socket.info.NodeUser;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Sloriac on 2016/12/18.
 */
public interface IDataNodeAction {

    public void updateActiveNode(NodeInfo activeNode);

    public LinkedList<NodeInfo> getActiveNodesInfo();

    public NodeInfo getLeaderNode();

    public void updateFromLeaderFiles(List<FileMeta> files);

    public List<FileMeta> getFilesInfo();

    public FileMeta popuFile(FileMeta fileMeta);

    public void updateLeader();

    public NodeInfo getSelfNode();

    public void writeRemoteFile(FileMeta fileMeta);

    public void writeLocalFile(File file);

    public void updateActiveNodes(LinkedList<NodeInfo> activeNodes);

    public NodeInfo getFreeNode();

    public List<NodeUser> getNodeUsers();

    //数据节点用的
    public void updateNodeUsers(List<NodeUser> nodeUsers);

    //leader节点用的
    public void updateNodeUser(NodeUser nodeUser);

    public NodeUser getMyUser();

    public void addUser();

    public void removeUser();

    // 数据节点
    public void updateActiveNodesLastTime(HashMap<String, Long> activeNodesLastTime);

    // leader节点
    public void updateActiveNodesLastTime(String id, Long time);

    public HashMap<String, Long> getActiveNodesLastTime();

    public void stop();

    public void restart();

}
