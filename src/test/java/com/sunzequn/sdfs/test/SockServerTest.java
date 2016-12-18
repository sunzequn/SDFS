package com.sunzequn.sdfs.test;

import com.sunzequn.sdfs.node.DataNode;
import com.sunzequn.sdfs.node.NodeInfo;


/**
 * Created by Sloriac on 2016/12/18.
 */
public class SockServerTest {

    public static void main(String[] args) {
        NodeInfo leader = new NodeInfo("0", "localhost", 1111);
        DataNode dataNode = new DataNode(leader, true, "C:\\Users\\Sloriac\\Desktop\\code\\SDFS\\src\\main\\resources\\0\\");
        dataNode.start();
    }
}
