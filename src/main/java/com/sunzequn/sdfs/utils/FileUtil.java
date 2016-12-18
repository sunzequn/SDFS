package com.sunzequn.sdfs.utils;

import java.io.*;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class FileUtil {

    public static void copyFile(byte[] content, String dest) {
        InputStream input = byte2Input(content);
        copyFile(input, dest);
    }

    public static byte[] copyFile(InputStream input, String dest) {
        OutputStream output;
        try {
            byte[] res = input2byte(input);
            output = new FileOutputStream(new File(dest));
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
            output.close();

            input.close();
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    public static byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }
}
