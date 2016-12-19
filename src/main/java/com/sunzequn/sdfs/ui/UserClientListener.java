package com.sunzequn.sdfs.ui;


import com.sunzequn.sdfs.file.FileMeta;
import com.sunzequn.sdfs.rmi.ClientTimeThread;
import com.sunzequn.sdfs.rmi.RemoteClient;
import com.sunzequn.sdfs.utils.FileUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by sloriac on 16-12-19.
 */
public class UserClientListener implements ActionListener {


    private JScrollPane upperPanel;
    private JTextField portTextField;
    private JButton connectButton;
    private JLabel infoLabel;
    ClientTimeThread clientTimeThread;

    public UserClientListener(JScrollPane upperPanel, JTextField portTextField, JButton connectButton, JLabel infoLabel) {
        this.upperPanel = upperPanel;
        this.portTextField = portTextField;
        this.connectButton = connectButton;
        this.infoLabel = infoLabel;
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
            DataNodeUrl.dataNodeUrl = remoteClient.getNode();
            System.out.println(DataNodeUrl.dataNodeUrl);
            remoteClient = new RemoteClient(DataNodeUrl.dataNodeUrl);
            //获取ip注册一下
            String ip = remoteClient.getIp();
            display(remoteClient, ip);
            List<FileMeta> files = remoteClient.getFiles();
            if (files != null && files.size() > 0) {
                showFiles(files);
            }
            connectButton.setText("离开");
        } else {
            infoLabel.setText("参数错误，请重试！");
        }
    }

    private void display(RemoteClient remoteClient, String ip) throws RemoteException {
        ClientTimeThread clientTimeThread = new ClientTimeThread(remoteClient, infoLabel, ip);
        clientTimeThread.start();
    }

    private void handleExitButton() {
        if (DataNodeUrl.dataNodeUrl == null) return;
        RemoteClient remoteClient = new RemoteClient(DataNodeUrl.dataNodeUrl);
        remoteClient.exit();
        connectButton.setText("连接");
    }

    private void showFiles(List<FileMeta> files) {

        JPanel jPanel = new JPanel();
        jPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        for (FileMeta file : files) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            JLabel jLabel = new JLabel();
            jLabel.setText(FileUtil.getFixedLenString(file.getName() + "  " + file.getTimestamp(), 50, ' '));
            JButton fileButton = new JButton();
            fileButton.setText("下 载");
            fileButton.addActionListener(new DownloadListener(upperPanel, file));
            panel.add(jLabel);
            panel.add(fileButton);
            jPanel.add(panel);
        }
        upperPanel.setViewportView(jPanel);
    }

}
