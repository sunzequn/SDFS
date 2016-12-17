package com.sunzequn.sdfs.test;

import com.sunzequn.sdfs.file.FileHandler;
import com.sunzequn.sdfs.file.FileMeta;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Sloriac on 16/12/17.
 */
public class FileTest {

    private String srcPath = "/Users/Sloriac/Programing/github/SDFS/src/main/java/com/sunzequn/sdfs/file/FileMeta.java";
    private String descPath = "/Users/Sloriac/FileMeta.java";

    @Test
    public void testFileMeta(){
        FileMeta fileMeta = new FileMeta(srcPath, "", "");
        System.out.println(fileMeta);
    }

    @Test
    public void testWriteLocalFile() throws Exception {
        FileHandler fileHandler = new FileHandler();
        fileHandler.writeLocalFile(srcPath, descPath, "");
        String srcMD5 = DigestUtils.md5Hex(new FileInputStream(new File(srcPath)));
        String descMD5 = DigestUtils.md5Hex(new FileInputStream(new File(descPath)));
        Assert.assertEquals("文件一致性检查", srcMD5, descMD5);
    }
}
