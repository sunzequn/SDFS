package com.sunzequn.sdfs.socket.client;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sloriac on 2016/12/16.
 *
 */
public class KeepAlive implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\t维持连接";
    }
}