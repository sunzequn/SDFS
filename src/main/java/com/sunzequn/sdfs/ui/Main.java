package com.sunzequn.sdfs.ui;

import com.sunzequn.sdfs.node.DataNode;
import com.sunzequn.sdfs.node.NodeInfo;
import com.sunzequn.sdfs.utils.PropertiesUtil;

import javax.swing.*;
import java.util.Arrays;

/**
 * Created by sloriac on 16-12-20.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            UserClient myFrame = new UserClient("客户端");
            myFrame.setSize(400, 260);
            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myFrame.setVisible(true);
        } else {
            String path = args[0];
            PropertiesUtil util = null;
            try {
                util = new PropertiesUtil(path);
                String id = util.getValue("id");
                String localPort = util.getValue("port");
                String folder = util.getValue("folder");
                String isLeader = util.getValue("isLeader");
                if (!isLeader.equals("1")) {
                    String leaderId = util.getValue("leaderId");
                    String leaderIp = util.getValue("leaderIp");
                    String leaderPort = util.getValue("leaderPort");

                    NodeInfo selfInfo = new NodeInfo(id, "localhost", Integer.parseInt(localPort));
                    NodeInfo leader = new NodeInfo(leaderId, leaderIp, Integer.parseInt(leaderPort));
                    DataNode dataNode = new DataNode(selfInfo, leader, folder);
                    dataNode.start(true);

                } else {
                    NodeInfo leader = new NodeInfo(id, "localhost", Integer.parseInt(localPort));
                    DataNode dataNode = new DataNode(leader, true, folder);
                    dataNode.start(true);
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("配置文件有误");
            }


        }

    }
}
