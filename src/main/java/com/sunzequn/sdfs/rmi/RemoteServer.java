package com.sunzequn.sdfs.rmi;

import com.sunzequn.sdfs.node.IDataNodeAction;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by sloriac on 16-12-19.
 */
public class RemoteServer {

    private IDataNodeAction nodeAction;
    private IRemote remote;
    private int port;
    private String name;

    public RemoteServer(IDataNodeAction nodeAction, int port) {
        this.nodeAction = nodeAction;
        this.port = port;
        this.name = "rmi://localhost:" + port + "/rmi";
        run();

    }

    public void run() {
        try {
            remote = new RemoteImpl(nodeAction);
            LocateRegistry.createRegistry(port);
            Naming.bind(name, remote);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("服务端启动成功！");
    }

    public void stop() throws RemoteException, NotBoundException, MalformedURLException {
        Naming.unbind(name);
        System.out.println("服务端已停止！");
    }

    public IRemote getRemote() {
        return remote;
    }
}
