package com.sunzequn.sdfs.test;

import com.sunzequn.sdfs.node.DataNode;
import com.sunzequn.sdfs.node.NodeInfo;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class SockClient2Test {

    public static void main(String[] args) {
        NodeInfo selfInfo = new NodeInfo("2", "localhost", 1002);
        NodeInfo leader = new NodeInfo("0", "localhost", 1111);
        DataNode dataNode = new DataNode(selfInfo, leader, "C:\\Users\\Sloriac\\Desktop\\code\\SDFS\\src\\main\\resources\\2\\");
        dataNode.start();
    }
}
