package com.sunzequn.sdfs.ui;

import com.sunzequn.sdfs.file.FileMeta;
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
public class DownloadListener implements ActionListener {

    private JScrollPane upperPanel;
    private FileMeta fileMeta;

    public DownloadListener(JScrollPane upperPanel, FileMeta fileMeta) {
        this.upperPanel = upperPanel;
        this.fileMeta = fileMeta;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setSelectedFile(new File(fileMeta.getName()));
        jFileChooser.setDialogTitle("保存");
        int result = jFileChooser.showDialog(upperPanel, "保存文件");
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            if (DataNodeUrl.dataNodeUrl != null) {
                System.out.println(DataNodeUrl.dataNodeUrl);
                RemoteClient remoteClient = new RemoteClient(DataNodeUrl.dataNodeUrl);
                System.out.println(remoteClient);
                try {
                    byte[] contents = remoteClient.downloadFile(fileMeta.getName());
                    FileUtils.writeByteArrayToFile(file, contents);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
