package com.sunzequn.sdfs.test;

import com.sunzequn.sdfs.node.DataNode;
import com.sunzequn.sdfs.node.NodeInfo;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class SockClientTest {

    public static void main(String[] args) {
        NodeInfo selfInfo = new NodeInfo("2", "localhost", 1122);
        NodeInfo leader = new NodeInfo("leader", "localhost", 1111);
        DataNode dataNode = new DataNode(selfInfo, leader);
        dataNode.start();
    }
}
