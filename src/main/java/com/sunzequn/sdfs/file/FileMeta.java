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
    private String localName;
    private String timestamp;
    private long size;
    private String srcNode;
    private String md5;
    // meta时候为null
    private List<String> contents;

    public FileMeta(String name, String localName, String timestamp, long size, String srcNode, List<String> contents) {
        try {
            this.name = name;
            this.localName = localName;
            this.timestamp = timestamp;
            this.size = size;
            this.srcNode = srcNode;
            this.contents = contents;
            this.md5 = DigestUtils.md5Hex(contents.get(contents.size() - 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSrcNode() {
        return srcNode;
    }

    public void setSrcNode(String srcNode) {
        this.srcNode = srcNode;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
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
                ", localName='" + localName + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", size=" + size +
                ", srcNode='" + srcNode + '\'' +
                ", md5='" + md5 + '\'' +
                ", contents=" + Arrays.toString(contents) +
                '}';
    }
}
