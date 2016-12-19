package com.sunzequn.sdfs.test;

import com.sunzequn.sdfs.node.DataNode;
import com.sunzequn.sdfs.node.NodeInfo;

import java.io.File;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class SockClient1Test {

    public static void main(String[] args) {
        NodeInfo selfInfo = new NodeInfo("1", "localhost", 9999);
        NodeInfo leader = new NodeInfo("0", "localhost", 1111);
        DataNode dataNode = new DataNode(selfInfo, leader, "/home/sloriac/data/1/");
        dataNode.start(true);
    }
}
