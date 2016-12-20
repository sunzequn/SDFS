package com.sunzequn.sdfs.utils;


import java.io.*;
import java.util.Properties;

/**
 * Created by Sloriac on 16/12/20.
 * <p>
 * 读取Properties配置文件的工具类
 */
public class PropertiesUtil {

    private Properties properties = new Properties();

    public PropertiesUtil(String filePath) throws Exception {
        InputStream inputStream = new FileInputStream(new File(filePath));
        this.properties.load(inputStream);
    }

    /**
     * 从配置文件中根据键获得值
     *
     * @param key 键
     * @return 如果键值对存在则返回值, 否则返回null
     */
    public String getValue(String key) {
        return properties.getProperty(key);
    }
}
