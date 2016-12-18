package com.sunzequn.sdfs.node;

import com.sunzequn.sdfs.file.FileMeta;

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

    public void writeLocalFile(String path);

    public void updateActiveNodes(LinkedList<NodeInfo> activeNodes);
}
