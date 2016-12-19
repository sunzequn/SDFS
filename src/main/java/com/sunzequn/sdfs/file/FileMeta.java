package com.sunzequn.sdfs.file;

import com.sunzequn.sdfs.utils.FileUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sloriac on 16/12/17.
 *
 */
public class FileMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String timestamp;
    private long size;
    private String srcNode;
    private String md5;
    // meta时候为null
    private byte[] contents;

    public FileMeta(String name, String timestamp, long size, String srcNode, byte[] contents) {
        try {
            this.name = name;
            this.timestamp = timestamp;
            this.size = size;
            this.srcNode = srcNode;
            this.contents = contents;
            this.md5 = DigestUtils.md5Hex(contents);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public long getSize() {
        return size;
    }

    public String getSrcNode() {
        return srcNode;
    }

    public String getMd5() {
        return md5;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "name='" + name + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", size=" + size +
                ", srcNode='" + srcNode + '\'' +
                ", md5='" + md5 + '\'' +
                ", contents=" + Arrays.toString(contents) +
                '}';
    }
}
