package com.sunzequn.sdfs.test;

import com.sunzequn.sdfs.utils.TimeUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sloriac on 16-12-19.
 */
public class TimeTest {

    @Test
    public void testTimestampCompare() throws InterruptedException {
        String t1 = TimeUtil.generateTime();
        Thread.sleep(1111);
        String t2 = TimeUtil.generateTime();
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t2.compareTo(t1));
        System.out.println(t1.compareTo(t2));
    }
}
