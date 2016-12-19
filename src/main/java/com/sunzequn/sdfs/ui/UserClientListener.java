package com.sunzequn.sdfs.ui;


import com.sunzequn.sdfs.rmi.ClientTimeThread;
import com.sunzequn.sdfs.rmi.RemoteClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

/**
 * Created by sloriac on 16-12-19.
 */
public class UserClientListener implements ActionListener {


    private JTextField portTextField;
    private JButton connectButton;
    private JButton exitButton;
    private JLabel infoLabel;
    private String dataNodeUrl;
    ClientTimeThread clientTimeThread;

    public UserClientListener(JTextField portTextField, JButton connectButton, JButton exitButton, JLabel infoLabel) {
        this.portTextField = portTextField;
        this.connectButton = connectButton;
        this.infoLabel = infoLabel;
        this.exitButton = exitButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        try {
            switch (command) {
                case "连接":
                    handleConnectButton();
                    break;
                case "离开":
                    handleExitButton();
                    break;
                default:
                    connectButton.setText("连接");
                    infoLabel.setText("连接已终止");
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            infoLabel.setText("连接失败，请重试！");
        }
    }

    private void handleConnectButton() throws RemoteException {
        String port = portTextField.getText();
        if (!port.equals("") && port.contains(":")) {
            //链接leader请求分配数据节点
            RemoteClient remoteClient = new RemoteClient(port);
            //获得数据节点
            dataNodeUrl = remoteClient.getNode();
            System.out.println(dataNodeUrl);
            remoteClient = new RemoteClient(dataNodeUrl);
            //获取ip注册一下
            String ip = remoteClient.getIp();
            display(remoteClient, ip);
        } else {
            infoLabel.setText("参数错误，请重试！");
        }
    }

    private void display(RemoteClient remoteClient, String ip) throws RemoteException {
        ClientTimeThread clientTimeThread = new ClientTimeThread(remoteClient, infoLabel, ip);
        clientTimeThread.start();
    }

    private void handleExitButton() {
        if (dataNodeUrl == null) return;
        RemoteClient remoteClient = new RemoteClient(dataNodeUrl);
        remoteClient.exit();
    }

}
