package com.sunzequn.sdfs.socket.server;

import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.socket.info.Ask4File;
import com.sunzequn.sdfs.socket.info.KeepAlive;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by Sloriac on 2016/12/16.
 */
public class SockServerHandler extends Thread {

    private Socket socket;
    private SockServer sockServer;

    public SockServerHandler(Socket socket, SockServer sockServer) {
        this.socket = socket;
        this.sockServer = sockServer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                InputStream in = socket.getInputStream();
                if (in.available() > 0) {
                    ObjectInputStream ois = new ObjectInputStream(in);
                    Object obj = ois.readObject();
                    // 心跳测试
                    if (obj instanceof KeepAlive) {
//                        System.out.println("客户端心跳: " + obj.toString());
                        sockServer.handleHeart((KeepAlive) obj, socket);
                    }
                    // 新文件传输
                    else if (obj instanceof FileMeta) {
                        sockServer.handleNewFile((FileMeta) obj);
                    } else if (obj instanceof Ask4File) {
                        sockServer.downloadFile((Ask4File) obj);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
