package com.sunzequn.sdfs.test;

import com.sunzequn.sdfs.node.DataNode;
import com.sunzequn.sdfs.node.NodeInfo;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class SockClient1Test {

    public static void main(String[] args) {
        NodeInfo selfInfo = new NodeInfo("1", "localhost", 1001);
        NodeInfo leader = new NodeInfo("0", "localhost", 1111);
        DataNode dataNode = new DataNode(selfInfo, leader, "/Users/Sloriac/Programing/github/SDFS/1/");
        dataNode.start();
        dataNode.writeLocalFile("/Users/Sloriac/Programing/github/SDFS/src/test/java/com/sunzequn/sdfs/test/SockClient1Test.java");
    }
}
