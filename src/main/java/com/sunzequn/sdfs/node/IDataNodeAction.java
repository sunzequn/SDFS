package com.sunzequn.sdfs.node;

import com.sunzequn.sdfs.file.FileMeta;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sloriac on 2016/12/18.
 */
public interface IDataNodeAction {

    public void updateActiveNodes(LinkedList<NodeInfo> activeNodes);

    public LinkedList<NodeInfo> getActiveNodesInfo();

    public void updateFiles(List<FileMeta> files);

    public List<FileMeta> getFilesInfo();

    public void updateLeader();

    public NodeInfo getSelfNode();


}
