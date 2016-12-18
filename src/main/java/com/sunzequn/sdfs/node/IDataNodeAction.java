package com.sunzequn.sdfs.node;

import com.sunzequn.sdfs.file.FileMeta;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Sloriac on 2016/12/18.
 */
public interface IDataNodeAction {

    public void updateActiveNodes(LinkedList<NodeInfo> activeNodes);

    public LinkedList<NodeInfo> getActiveNodesInfo();

    public NodeInfo getLeaderNode();

    public void updateFiles(List<FileMeta> files);

    public List<FileMeta> getFilesInfo();

    public void updateLeader();

    public NodeInfo getSelfNode();

}
