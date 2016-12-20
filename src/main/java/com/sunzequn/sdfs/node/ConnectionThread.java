package com.sunzequn.sdfs.node;

import com.sunzequn.sdfs.socket.client.SockClient;
import com.sunzequn.sdfs.socket.info.NodeUser;

import java.util.List;

/**
 * Created by sloriac on 16-12-20.
 */
public class ConnectionThread extends Thread {

    private IDataNodeAction dataNodeAction;

    public ConnectionThread(IDataNodeAction dataNodeAction) {
        this.dataNodeAction = dataNodeAction;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            System.out.println("节点负载检查");
            List<NodeUser> nodeUsers = dataNodeAction.getNodeUsers();
            if (nodeUsers.size() > 0) {
                int total = 0;
                for (NodeUser nodeUser : nodeUsers) {
                    total += nodeUser.getNum();
                }
                double avg = total / nodeUsers.size();
                if (avg < 3) {
                    NodeUser lastNode = nodeUsers.get(nodeUsers.size() - 1);
                    if (lastNode.getNum() == 0) {
                        dataNodeAction.stop(lastNode);
                    }
                }
            }
            try {
                Thread.sleep(20 * SockClient.getDELAY());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
