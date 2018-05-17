package org.xiaoheshan.hallo.boxing.client.ui.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 简介
 *
 * @author : _Chf
 * @since : 11-05-2018
 */
public abstract class DateUtils {

    public static final String PATTERN_0 = "yyyy-MM-dd hh:mm:ss";

    public static Date now() {
        return new Date();
    }

    public static String format(Date date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.format(DateUtils.now(), DateUtils.PATTERN_0));
    }
}
