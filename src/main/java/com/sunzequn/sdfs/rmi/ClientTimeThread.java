package com.sunzequn.sdfs.rmi;


import javax.swing.*;
import java.util.Date;

/**
 *
 */
public class ClientTimeThread extends Thread {

    private RemoteClient client;
    private JLabel infoLabel;
    private String ip;

    public ClientTimeThread(RemoteClient client, JLabel infoLabel, String ip) {
        this.client = client;
        this.infoLabel = infoLabel;
        this.ip = ip;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String time = client.getTime();
                if (time != null) {
                    infoLabel.setText(time + "  From  " + ip);
                } else {
                    infoLabel.setText("连接异常！");
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.getCause();
        }
    }
}
