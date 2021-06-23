package com.example.module_homepage2.base;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期时间工具类，进行各种日期时间格式的转化以及格式化
 */

public class DateTimeUtils {
    public final static int TIME_DAY_MILLISECOND = 86400000;
    // /
    // 定义时间日期显示格式
    // /
    public final static String DATE_FORMAT = "yyyy-MM-dd";

    public final static String DATE_FORMAT_CN = "yyyy年MM月dd日";

    public final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String TIME_FORMAT_MONTH2MINUTE = "MM-dd HH:mm";

    private final static String TIME_FORMAT_CN = "yyyy年MM月dd日 HH:mm:ss";

    private final static String MONTH_FORMAT = "yyyy-MM";

    private final static String MONTH_FORMAT_2 = "yyyyMM";

    private final static String MONTH_FORMAT_2_CN = "yyyy年MM月";

    public final static String DAY_FORMAT = "yyyyMMdd";

    private final static String DAY_FORMAT_CN = "MM月dd日";

    private final static String MIN_FORMAT = "HH:mm:ss";

    private final static String MIN_FORMAT_2 = "HHmmss";


    public static Date getDateBeforeOrAfterMonth(Date curDate, int iDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(curDate);
        cal.add(Calendar.MONTH, iDate);
        return cal.getTime();
    }

    /**
     * 获取当前系统时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT);
        return df.format(new Date());
    }

    /**
     * 获取当前系统时间
     *
     * @return MM-dd HH:mm
     */
    public static String getCurrentTimeWithMonth2Minute() {
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT_MONTH2MINUTE);
        return df.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return yyyy年MM月dd日
     */
    public static String getCurrentFormatDay() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return new SimpleDateFormat(DATE_FORMAT_CN).format(date);
    }

    /**
     * 获取当前日期
     *
     * @return yyyyMMdd
     */
    public static String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return new SimpleDateFormat(DAY_FORMAT).format(date);
    }
    /**
     *
     *格式化日期 yyyyMM to yyyy年MM月dd日
     * @return yyyy年MM月dd日
     */
    public static String formatDateStr(String dateStr) {
        if ("".equals(dateStr))
            return dateStr;

        if ("-".equals(dateStr))
            return dateStr;

        SimpleDateFormat sf1 = new SimpleDateFormat(DAY_FORMAT);
        SimpleDateFormat sf2 = new SimpleDateFormat(DATE_FORMAT_CN);
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
            return "-";
        }
        return sfstr;
    }


    /**
     * 格式化日期 yyyyMM to yyyy年MM月
     *
     * @param str 字符yyyyMM
     * @return yyyy年MM月
     */
    public static String formatYear(String str) {
        if ("".equals(str))
            return str;

        if ("-".equals(str))
            return str;

        SimpleDateFormat sf1 = new SimpleDateFormat(MONTH_FORMAT_2);
        SimpleDateFormat sf2 = new SimpleDateFormat(MONTH_FORMAT_2_CN);
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return "-";
        }
        return sfstr;
    }

    /**
     * 格式化日期 yyyyMMdd to MM月dd日
     *
     * @param str yyyyMMdd
     * @return MM月dd日
     */
    public static String formatRateDate(String str) {
        if ("".equals(str))
            return str;

        SimpleDateFormat sf1 = new SimpleDateFormat(DAY_FORMAT);
        SimpleDateFormat sf2 = new SimpleDateFormat(DAY_FORMAT_CN);
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return "-";
        }
        return sfstr;
    }

    /**
     * 格式化日期 yyyyMMdd to yyyy-MM-dd
     *
     * @param str yyyyMMdd
     * @return yyyy-MM-dd
     */
    public static String formatDate(String str) {
        if ("".equals(str))
            return str;

        if ("-".equals(str))
            return str;

        SimpleDateFormat sf1 = new SimpleDateFormat(DAY_FORMAT);
        SimpleDateFormat sf2 = new SimpleDateFormat(DATE_FORMAT);
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return "-";
        }
        return sfstr;
    }

    /**
     * 格式化日期 yyyyMMdd to yyyy-MM
     *
     * @param str yyyyMMdd
     * @return yyyy-MM
     */
    public static String formatDateMiddleLine(String str) {
        if ("".equals(str))
            return str;

        if ("-".equals(str))
            return str;

        SimpleDateFormat sf1 = new SimpleDateFormat(DAY_FORMAT);
        SimpleDateFormat sf2 = new SimpleDateFormat(MONTH_FORMAT);
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return "-";
        }
        return sfstr;
    }


    /**
     * 格式化时间 17364542 to 17:36:45
     *
     * @param str 非正常的毫秒字符串前二位字符表示HH，2-4位表示mm,4-6位表示ss
     * @return HH:mm:ss
     */
    public static String formatTime(String str) {
        if ("".equals(str) || str.length() < 6)
            return str;
        SimpleDateFormat sf1 = new SimpleDateFormat(MIN_FORMAT_2);
        SimpleDateFormat sf2 = new SimpleDateFormat(MIN_FORMAT);
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(str.substring(0, 6)));
        } catch (ParseException e) {
            e.printStackTrace();
            return "-";
        }
        return sfstr;
    }

    /**
     * 将当格式的日期时间转换成想要的格式
     *
     * @param str           日期时间字符串
     * @param currentFormat 当前时间格式
     * @param toFormat      目的时间格式
     * @return
     */
    public static String formatDate(String str, String currentFormat, String toFormat) {
        if (TextUtils.isEmpty(currentFormat) || TextUtils.isEmpty(str) || TextUtils.isEmpty(toFormat))
            throw new RuntimeException("formatData str or format is null");

        SimpleDateFormat sf1 = new SimpleDateFormat(currentFormat);
        SimpleDateFormat sf2 = new SimpleDateFormat(toFormat);
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return "-";
        }
        return sfstr;
    }

    /**
     * 传入的时间格式必须类似于2012-8-21 17:53:20这样的格式
     *
     * @param createtime
     * @return
     */
    public static String getInterval(String createtime) {
        if (TextUtils.isEmpty(createtime))
            return "";

        String interval = null;

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date d1 = (Date) sd.parse(createtime, pos);

        //用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
        long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒

        if (time / 1000 < 10 && time / 1000 >= 0) {
            //如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
            interval = "刚刚";

        } else if (time / 3600000 < 24 && time / 3600000 > 0) {
            //如果时间间隔小于24小时则显示多少小时前
            int h = (int) (time / 3600000);//得出的时间间隔的单位是小时
            interval = h + "小时前";

        } else if (time / 60000 < 60 && time / 60000 > 0) {
            //如果时间间隔小于60分钟则显示多少分钟前
            int m = (int) ((time % 3600000) / 60000);//得出的时间间隔的单位是分钟
            interval = m + "分钟前";

        } else if (time / 1000 < 60 && time / 1000 > 0) {
            //如果时间间隔小于60秒则显示多少秒前
            int se = (int) ((time % 60000) / 1000);
            interval = se + "秒前";

        } else {
            //大于24小时，则显示正常的时间，但是不显示秒
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            ParsePosition pos2 = new ParsePosition(0);
            Date d2 = (Date) sdf.parse(createtime, pos2);

            interval = sdf.format(d2);
        }
        return interval;
    }

    public static String urlWithTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        return sdf.format(new Date());
    }
}
