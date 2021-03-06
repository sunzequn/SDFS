package com.sunzequn.sdfs.rmi;

import com.sunzequn.sdfs.file.FileMeta;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by sloriac on 16-12-19.
 */
public class RemoteClient {

    private String name;

    public RemoteClient(String ipPort) {
        this.name = "rmi://" + ipPort + "/rmi";
    }

    public String getTime() throws RemoteException, NotBoundException, MalformedURLException {
        IRemote remote = (IRemote) Naming.lookup(name);
        return remote.generateTime();
    }

    public String getIp() {
        try {
            IRemote remote = (IRemote) Naming.lookup(name);
            return remote.getIp();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNode() throws RemoteException, NotBoundException, MalformedURLException {
        IRemote remote = (IRemote) Naming.lookup(name);
        return remote.getNodeHost();
    }

    public void exit() {
        try {
            IRemote remote = (IRemote) Naming.lookup(name);
            remote.exit();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<FileMeta> getFiles() throws RemoteException, NotBoundException, MalformedURLException {
        IRemote remote = (IRemote) Naming.lookup(name);
        return remote.getFiles();
    }

    public void uploadFile(File file, byte[] contents) {
        try {
            IRemote remote = (IRemote) Naming.lookup(name);
            remote.uploadFile(file, contents);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public int getTotalUserNum() {
        try {
            IRemote remote = (IRemote) Naming.lookup(name);
            return remote.getTotalUserNum();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public byte[] downloadFile(String fileName) {
        try {
            IRemote remote = (IRemote) Naming.lookup(name);
            return remote.downloadFile(fileName);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stop() {
        try {
            IRemote remote = (IRemote) Naming.lookup(name);
            remote.stop();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            IRemote remote = (IRemote) Naming.lookup(name);
            remote.restart();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
