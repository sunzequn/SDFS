package com.sunzequn.sdfs.ui;

import com.sunzequn.sdfs.rmi.RemoteClient;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by sloriac on 16-12-19.
 */
public class UploadListener implements ActionListener {

    private JPanel midPanel;

    public UploadListener(JPanel midPanel) {
        this.midPanel = midPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("选择文件");
        int result = jFileChooser.showDialog(midPanel, "选择文件");
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            if (DataNodeUrl.dataNodeUrl != null) {
                RemoteClient remoteClient = new RemoteClient(DataNodeUrl.dataNodeUrl);
                try {
                    System.out.println(file.getPath());
                    remoteClient.uploadFile(file, FileUtils.readFileToByteArray(file));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }
}
