package com.sunzequn.sdfs.file;

import com.sunzequn.sdfs.utils.FileUtil;
import com.sunzequn.sdfs.utils.TimeUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sloriac on 16/12/17.
 */
public class FileHandler {

    private String folder;
    private String nodeId;

    public FileHandler(String folder, String nodeId) {
        this.folder = folder;
        this.nodeId = nodeId;
    }

    /**
     * 将本地的源文件写入目标文件
     * @param srcPath
     * @return
     */
    public FileMeta writeLocalFile(String srcPath) {
        try {
            File srcFile = new File(srcPath);
            InputStream inputStream = new FileInputStream(srcFile);
            long size = srcFile.length();
            String name = srcFile.getName();
            String timestamp = TimeUtil.generateTime();
            String localName = generateLocalName(name, timestamp);
            byte[] content = FileUtil.copyFile(inputStream, folder + localName);
            return new FileMeta(name, localName, timestamp, size, nodeId, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public FileMeta popuFile(FileMeta fileMeta) {
        try {
            String path = folder + fileMeta.getLocalName();
            File file = new File(path);
            fileMeta.setContents(FileUtil.input2byte(new FileInputStream(file)));
            return fileMeta;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeRemoteFile(FileMeta fileMeta) {
        try {
            FileUtil.copyFile(fileMeta.getContents(), folder + fileMeta.getLocalName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateLocalName(String name, String timestamp) {
        return Base64.encodeBase64String((timestamp + name).getBytes());
    }


}
