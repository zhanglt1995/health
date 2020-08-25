package com.zlt.health.util;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 日期操作工具类
 */
public class DateUtils {
    /**
     * 日期转换-  String -> Date
     *
     * @param dateString 字符串时间
     * @return Date类型信息
     * @throws Exception 抛出异常
     */
    public static Date parseString2Date(String dateString) throws Exception {
        if (dateString == null) {
            return null;
        }
        return parseString2Date(dateString, "yyyy-MM-dd");
    }

    /**
     * 日期转换-  String -> Date
     *
     * @param dateString 字符串时间
     * @param pattern    格式模板
     * @return Date类型信息
     * @throws Exception 抛出异常
     */
    public static Date parseString2Date(String dateString, String pattern) throws Exception {
        if (dateString == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = sdf.parse(dateString);
        return date;
    }

    /**
     * 日期转换 Date -> String
     *
     * @param date Date类型信息
     * @return 字符串时间
     * @throws Exception 抛出异常
     */
    public static String parseDate2String(Date date) throws Exception {
        if (date == null) {
            return null;
        }
        return parseDate2String(date, "yyyy-MM-dd");
    }

    /**
     * 日期转换 Date -> String
     *
     * @param date    Date类型信息
     * @param pattern 格式模板
     * @return 字符串时间
     * @throws Exception 抛出异常
     */
    public static String parseDate2String(Date date, String pattern) throws Exception {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String strDate = sdf.format(date);
        return strDate;
    }

    public static String getLocalDate(){
        return LocalDate.now().toString().substring(0,10);
    }

    /**
     * 获取当前日期的本周一是几号
     *
     * @return 本周一的日期
     */
    public static Date getThisWeekMonday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    /**
     * 获取当前日期周的最后一天
     *
     * @return 当前日期周的最后一天
     */
    public static Date getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        c.add(Calendar.DATE, -dayOfWeek + 7);
        return c.getTime();
    }

    /**
     * 根据日期区间获取月份列表
     *
     * @param minDate 开始时间
     * @param maxDate 结束时间
     * @return 月份列表
     * @throws Exception
     */
    public static List<String> getMonthBetween(String minDate, String maxDate, String format) throws Exception {
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        SimpleDateFormat sdf2 = new SimpleDateFormat(format);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf2.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }

    /**
     * 根据日期获取年度中的周索引
     *
     * @param date 日期
     * @return 周索引
     * @throws Exception
     */
    public static Integer getWeekOfYear(String date) throws Exception {
        Date useDate = parseString2Date(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(useDate);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取当前时间所在周的开始日期
     *
     * @return 开始时间
     */
    public static String getFirstDayOfWeek() {
        LocalDate localDate = LocalDate.now();
        String firstDayOfWeek = localDate.with(DayOfWeek.MONDAY).toString().substring(0, 10);
        return firstDayOfWeek;
    }

    /**
     * 获取当前时间所在周的结束日期
     *
     * @return 结束日期
     */
    public static String getLastDayOfWeek() {
        LocalDate localDate = LocalDate.now();
        String lastDayOfWeek = localDate.with(DayOfWeek.SUNDAY).toString().substring(0, 10);
        return lastDayOfWeek;
    }
    //获得上周一的日期
    public static Date geLastWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }

    //获得本周一的日期
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    //获得下周一的日期
    public static Date getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    //获得今天日期
    public static Date getToday(){
        return new Date();
    }

    //获得本月一日的日期
    public static String getFirstDay4ThisMonth(){
        LocalDate localDate = LocalDate.now();
        String firstday = localDate.with(TemporalAdjusters.firstDayOfMonth()).toString().substring(0, 10);
        return firstday;
    }

    public static void main(String[] args) {
        try {
            System.out.println("本周一" + parseDate2String(getThisWeekMonday()));
            System.out.println("本月一日" + getFirstDay4ThisMonth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLastDayOfThisMonth() {
        LocalDate localDate = LocalDate.now();
        String lastDay = localDate.with(TemporalAdjusters.lastDayOfMonth()).toString().substring(0, 10);
        return lastDay;
    }
}
