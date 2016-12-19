package com.sunzequn.sdfs.rmi;

import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.node.IDataNodeAction;
import com.sunzequn.sdfs.node.NodeInfo;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
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
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return (year + "/" + month + "/" + date + " " + hour + ":" + minute + ":" + second);
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


}
