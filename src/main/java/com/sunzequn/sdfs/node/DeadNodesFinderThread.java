package com.sunzequn.sdfs.node;

import com.sunzequn.sdfs.socket.client.SockClient;

import java.util.*;

/**
 * Created by sloriac on 16-12-19.
 */
public class DeadNodesFinderThread extends Thread {

    private IDataNodeAction dataNodeAction;

    public DeadNodesFinderThread(IDataNodeAction dataNodeAction) {
        this.dataNodeAction = dataNodeAction;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("失效节点检测");
            LinkedList<NodeInfo> activeNodes = dataNodeAction.getActiveNodesInfo();
            HashMap<String, Long> activeNodesLastTime = dataNodeAction.getActiveNodesLastTime();
            if (activeNodes.size() > 0) {
                //如果节点的上次活跃时间和当前时间相比，落后5个间隔，就认为该节点失效
                long now = System.currentTimeMillis();
                Set<String> deadIds = new HashSet<>();
                for (Map.Entry<String, Long> stringLongEntry : activeNodesLastTime.entrySet()) {
                    if (now - stringLongEntry.getValue() > 5 * SockClient.getDELAY()) {
                        System.out.println("节点 " + stringLongEntry.getKey() + " 失效");
                        deadIds.add(stringLongEntry.getKey());
                    }
                }
                dataNodeAction.removeDeadNode(deadIds);
            }
            try {
                Thread.sleep(5 * SockClient.getDELAY());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
