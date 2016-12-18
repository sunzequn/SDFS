package com.sunzequn.sdfs.utils;

import java.util.Calendar;

/**
 * Created by Sloriac on 2016/12/18.
 */
public class TimeUtil {

    public static String generateTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millsecond = calendar.get(Calendar.MILLISECOND);
        return (year + "/" + month + "/" + date + " " + hour + ":" + minute + ":" + second + ":" + millsecond);
    }
}
