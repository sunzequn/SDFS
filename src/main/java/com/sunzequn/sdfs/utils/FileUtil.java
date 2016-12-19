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

    /**
     * 获取定长的字符串
     *
     * @param str 原始字符串
     * @param len 固定长度
     * @param c   不够填充的字符
     * @return 固定长度的字符串
     */
    public static String getFixedLenString(String str, int len, char c) {
        if (str == null || str.length() == 0) {
            str = "";
        }
        if (str.length() == len) {
            return str;
        }
        if (str.length() > len) {
            return str.substring(0, len);
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < len) {
            sb.append(c);
        }
        return sb.toString();
    }
}
