package com.ssm.maven.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author 1034683568@qq.com
 * @project_name ssm-maven
 * @date 2017-3-1
 */
public class DateUtil {

    public static String formatDate(Date date, String format) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date != null) {
            result = sdf.format(date);
        }
        return result;
    }


    public static Date formatString(String str, String format) throws Exception {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(str);
    }

    public static String getCurrentDateStr() throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 格式时间转时间戳
     * @param date 时间
     * @param format 格式
     * @return Long 时间戳
     */
    public static long stringToStamp(String date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long res = 0L;
        try{
            Date d = sdf.parse(date);
            res = d.getTime() / 1000;
        }
        catch (ParseException e){
            return res;
        }
        return  res;
    }
    /**
     * 格式时间转时间戳--使用默认格式的重载
     * @param date 时间
     * @return Long 时间戳
     */
    public static long stringToStamp(String date){
        return stringToStamp(date,"yyyy-MM-dd");
    }

    /**
     * 时间戳转格式时间字符串
     * @param stamp 时间戳
     * @param format 格式
     * @return String 格式化时间
     */
    public static String stampToString(long stamp, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return  sdf.format(new Date(stamp * 1000));
    }

    public static String stampToString(long stamp){
        return stampToString(stamp,"yyyy-MM-dd");
    }

    public static long stringToStampZonedVersion(String date, String format){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDate ldt = LocalDate.from(dtf.parse(date));
        ZonedDateTime zdt = ZonedDateTime.of(ldt,LocalTime.of(0,0),ZoneId.systemDefault());
        return zdt.toEpochSecond();
    }

    public static String stampToStringZonedVersion(long stamp, String format){
        Instant instant = Instant.ofEpochSecond(stamp);
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        return dtf.format(zdt);
    }
}
