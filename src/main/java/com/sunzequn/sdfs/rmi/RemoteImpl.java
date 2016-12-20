package com.sunzequn.sdfs.rmi;

import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.node.IDataNodeAction;
import com.sunzequn.sdfs.node.NodeInfo;
import com.sunzequn.sdfs.utils.TimeUtil;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by sloriac on 16-12-19.
 */
public class RemoteImpl extends UnicastRemoteObject implements IRemote {

    private IDataNodeAction nodeAction;

    public RemoteImpl(IDataNodeAction nodeAction) throws RemoteException {
        this.nodeAction = nodeAction;
    }

    protected RemoteImpl() throws RemoteException {
    }

    @Override
    public String generateTime() throws RemoteException {
        return TimeUtil.generateTime();
    }

    @Override
    public String getIp() throws RemoteException {
        try {
            //节点用户数目自增
            nodeAction.addUser();
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FileMeta> getFiles() throws RemoteException {
        return nodeAction.getFilesInfo();
    }

    @Override
    public String getNodeHost() throws RemoteException {
        NodeInfo node = nodeAction.getFreeNode();
        if (node == null) return null;
        return node.getIp() + ":" + (node.getPort() + 1);
    }

    @Override
    public void exit() throws RemoteException {
        nodeAction.removeUser();
    }

    @Override
    public void uploadFile(File file, byte[] contents) throws RemoteException {
        nodeAction.uploadFile(file, contents);
    }

    @Override
    public int getTotalUserNum() throws RemoteException {
        return nodeAction.getTotalUserNum();
    }

    @Override
    public byte[] downloadFile(String name) throws RemoteException {
        return nodeAction.downloadFile(name);
    }

    @Override
    public void stop() throws RemoteException {
        nodeAction.stop();
    }

    @Override
    public void restart() throws RemoteException {
        nodeAction.restart();
    }


}
