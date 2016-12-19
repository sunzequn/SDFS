package com.sunzequn.sdfs.node;

import com.sunzequn.sdfs.socket.client.SockClient;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by sloriac on 16-12-19.
 */
public class DeadNodesFinderThread extends Thread {

    private LinkedList<NodeInfo> activeNodes;
    private HashMap<String, Long> activeNodesLastTime;
    private List<NodeInfo> deadNodes;

    public DeadNodesFinderThread(LinkedList<NodeInfo> activeNodes, HashMap<String, Long> activeNodesLastTime, List<NodeInfo> deadNodes) {
        this.activeNodes = activeNodes;
        this.activeNodesLastTime = activeNodesLastTime;
        this.deadNodes = deadNodes;
    }

    @Override
    public void run() {
        long total = 0;
        for (Map.Entry<String, Long> stringLongEntry : activeNodesLastTime.entrySet()) {
            total += stringLongEntry.getValue();
        }
        double avg = total / activeNodesLastTime.keySet().size();
        for (String s : activeNodesLastTime.keySet()) {
            double cha = activeNodesLastTime.get(s) - avg;
            if (cha > 0 && cha < 5 * SockClient.getDELAY()) {
                removeDeadNode(s);
            }
        }
    }

    private void removeDeadNode(String s) {
        LinkedList<NodeInfo> newNodes = new LinkedList<>();
        for (NodeInfo activeNode : activeNodes) {
            if (!activeNode.getId().equals(s))
                newNodes.add(activeNode);
            else
                deadNodes.add(activeNode);
        }
    }
}
