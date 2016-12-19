package com.sunzequn.sdfs.ui;

import com.sunzequn.sdfs.file.FileMeta;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by sloriac on 16-12-19.
 */
public class DownloadListener implements ActionListener {

    private JScrollPane upperPanel;
    private FileMeta fileMeta;
    private String remoteHost;

    public DownloadListener(JScrollPane upperPanel, FileMeta fileMeta, String remoteHost) {
        this.upperPanel = upperPanel;
        this.fileMeta = fileMeta;
        this.remoteHost = remoteHost;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser dlg = new JFileChooser();
        dlg.setSelectedFile(new File(fileMeta.getName()));
        dlg.setDialogTitle("保存");
        int result = dlg.showDialog(upperPanel, "保存文件");  // 打开"打开文件"对话框
// int result = dlg.showSaveDialog(this);  // 打"开保存文件"对话框
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = dlg.getSelectedFile();
            System.out.println(file.getPath());
        }
    }
}
