package com.sunzequn.sdfs.test;

import com.sunzequn.sdfs.node.DataNode;
import com.sunzequn.sdfs.node.NodeInfo;
import com.sunzequn.sdfs.socket.server.SockServer;


/**
 * Created by Sloriac on 2016/12/18.
 */
public class SockServerTest {

    public static void main(String[] args) {
        NodeInfo leader = new NodeInfo("leader", "localhost", 1111);
        DataNode dataNode = new DataNode(leader, true);
        dataNode.start();
    }
}
