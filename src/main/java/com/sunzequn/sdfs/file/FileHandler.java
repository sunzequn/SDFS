package com.sunzequn.sdfs.file;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Sloriac on 16/12/17.
 */
public class FileHandler {

    /**
     * 将本地的源文件写入目标文件
     * @param srcPath
     * @param descPath
     * @return
     */
    public FileMeta writeLocalFile(String srcPath, String descPath, String timestamp){
        try {
            File srcFile = new File(srcPath);
            File descFile = new File(descPath);
            FileUtils.copyFile(srcFile, descFile);
            return new FileMeta(srcPath, descPath, timestamp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FileMeta writeRemoteFile(File srcFile, String descPath, String timestamp){
        try {
            File descFile = new File(descPath);
            FileUtils.copyFile(srcFile, descFile);
            return new FileMeta(srcFile.getPath(), descPath, timestamp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
