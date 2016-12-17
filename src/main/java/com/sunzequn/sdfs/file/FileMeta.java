package com.sunzequn.sdfs.file;

import java.io.File;

/**
 * Created by Sloriac on 16/12/17.
 *
 */
public class FileMeta {

    private String name;
    private String timestamp;
    private long size;
    private String srcPath;
    private String descPath;
    private File file;

    public FileMeta(String srcPath, String descPath, String timestamp) {
        this.srcPath = srcPath;
        this.descPath = descPath;
        this.timestamp = timestamp;
        file = new File(srcPath);
        size = file.length();
        name = file.getName();
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "name='" + name + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", size=" + size +
                ", srcPath='" + srcPath + '\'' +
                ", descPath='" + descPath + '\'' +
                ", file=" + file +
                '}';
    }
}
