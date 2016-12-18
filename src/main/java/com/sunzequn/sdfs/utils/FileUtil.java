package com.sunzequn.sdfs.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class FileUtil {

    public static void copyFile(byte[] content, String dest) {
        try {
            FileUtils.writeByteArrayToFile(new File(dest), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
