package com.jgw.common_library.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 熊少武 on 2017/11/29 0029.
 * 时间格式化工具类(其他格式化方法另起其他工具类)
 */

@SuppressWarnings("unused")
public class FormatUtils {
    public static final String FULL_TIME_PATTERN="yyyy-MM-dd HH:mm:ss";
    public static final String DAY_TIME_PATTERN="yyyy-MM-dd";
    public static final String DEFAULT_TIME_PATTERN=FULL_TIME_PATTERN;
    public static String formatTime(long l) {
        return formatTime(l, DEFAULT_TIME_PATTERN);
    }

    public static String formatTime(long l, String strPattern) {
        Date date = new Date();
        date.setTime(l);
        return formatDate(date, strPattern);
    }

    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_TIME_PATTERN);
    }

    public static String formatDate(Date date, String strPattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
        return sdf.format(date);
    }

    public static Date decodeDate(String s) {
        return decodeDate(s, DEFAULT_TIME_PATTERN);
    }

    public static Date decodeDate(String s, String strPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
        Date format = null;
        try {
            format = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format;
    }

    public long decodeTime(String s) {
        return decodeTime(s, DEFAULT_TIME_PATTERN);
    }

    public long decodeTime(String s, String strPattern) {
        Date date = decodeDate(s, strPattern);
        return date == null ? 0 : date.getTime();
    }

    public static String formatMonthDay(String s) {
        long l = Long.parseLong(s);
        return formatTime(l, "MM月dd日");
    }

    public static String formatDateHour(long start, long end) {
        String strPattern = "yyyy-MM-dd HH时";
        return "·" + formatTime(start, strPattern) + "-" + formatTime(end, strPattern);
    }

    public static String formatDateToChineseCharacter(String date, String oldStrPattern, String newStrPattern) {
        Date oldDate = decodeDate(date, oldStrPattern);
        return formatDate(oldDate, newStrPattern);
    }

    public static String formatDateDay(long l) {
        return formatTime(l, DAY_TIME_PATTERN);
    }

    public static String formatDateWeek(long l) {
        return formatTime(l, "yyyy-MM-dd E");
    }

    public static String formatDateWeekHour(long l) {
        return formatTime(l, "yyyy-MM-dd E HH:mm");
    }

    public static String formatDateStartAndEnd(long start, long end) {
        String strPattern = "yyyy.MM.dd";
        return formatTime(start, strPattern) + "-" + formatTime(end, strPattern);
    }

    public static String formatLastDate(long l) {
        long time = (System.currentTimeMillis() - l) / 1000;
        if (time > (60 * 60 * 24 * 30 * 12)) {
            return "一年之前";
        }
        if (time > 60 * 60 * 24 * 30) {
            int month = (int) (time / (60 * 60 * 24 * 30));
            return month + "个月之前";
        }
        if (time > 60 * 60 * 24) {
            int day = (int) (time / (60 * 60 * 24));
            return day + "天之前";
        }
        if (time > 60 * 60) {
            int hour = (int) (time / (60 * 60));
            return hour + "小时之前";
        }
        if (time > 60) {
            int minute = (int) (time / 60);
            return minute + "分钟之前";
        }
        return "1分钟以内";
    }

    public static String formatAfterDate(long l) {
        long time = (l - System.currentTimeMillis()) / 1000;

        if (time > 60 * 60 * 24) {
            int day = (int) (time / (60 * 60 * 24));
            return "还剩" + day + "天";
        }

        if (time > 60 * 60) {
            int hour = (int) (time / (60 * 60));
            return "还剩" + hour + "小时";
        }
        if (time > 60) {
            int hour = (int) (time / 60);
            return "还剩" + hour + "分钟";
        }
        return "1分钟以后过期";
    }
}
