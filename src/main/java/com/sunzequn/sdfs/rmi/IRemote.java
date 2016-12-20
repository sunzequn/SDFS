package com.sunzequn.sdfs.rmi;

import com.sunzequn.sdfs.file.FileMeta;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by sloriac on 16-12-19.
 */
public interface IRemote extends Remote {

    public String generateTime() throws RemoteException;

    public String getIp() throws RemoteException;

    public List<FileMeta> getFiles() throws RemoteException;

    public String getNodeHost() throws RemoteException;

    public void exit() throws RemoteException;

    public void uploadFile(File file, byte[] contents) throws RemoteException;

    public int getTotalUserNum() throws RemoteException;

    public byte[] downloadFile(String name) throws RemoteException;

    public void stop() throws RemoteException;

    public void restart() throws RemoteException;
}
