package ru.tr1al.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = SECOND * 60 * 60;
    public static final long DAY = SECOND * 60 * 60 * 24;
    public static final long MONTH = DAY * 30;
    public static final long WEEK = DAY * 7;
    public static final long YEAR = MONTH * 12;

    public static Calendar getCalendar(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return c;
    }

    public static Calendar getCalendarNow() {
        return getCalendar(System.currentTimeMillis());
    }

    public static Calendar getCalendar(java.util.Date date) {
        return getCalendar(date.getTime());
    }

    public static long beginOfDay(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static Date beginOfDay(Date date) {
        return new Date(beginOfDay(date.getTime()));
    }

    public static java.sql.Date beginOfDay(java.sql.Date date) {
        return new java.sql.Date(beginOfDay(date.getTime()));
    }

    public static Timestamp beginOfDay(Timestamp time) {
        return new Timestamp(beginOfDay(time.getTime()));
    }

    public static long endOfDay(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTimeInMillis();
    }

    public static Date endOfDay(Date date) {
        return new Date(endOfDay(date.getTime()));
    }

    public static java.sql.Date endOfDay(java.sql.Date date) {
        return new java.sql.Date(endOfDay(date.getTime()));
    }

    public static Timestamp endOfDay(Timestamp time) {
        return new Timestamp(endOfDay(time.getTime()));
    }

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static java.sql.Date nowDate() {
        return new java.sql.Date(System.currentTimeMillis());
    }

    public static Timestamp timeFromNow(long x) {
        return new Timestamp(System.currentTimeMillis() + x);
    }

    public static java.sql.Date dateFromNow(long x) {
        return new java.sql.Date(System.currentTimeMillis() + x);
    }

    public static Timestamp timeFromDate(Timestamp date, long x) {
        if (date == null) return now();
        return new Timestamp(date.getTime() + x);
    }

    public static boolean isToday(Timestamp t1) {
        return isToday(new java.sql.Date(t1.getTime()));
    }

    public static boolean isToday(java.sql.Date dt1) {
        java.sql.Date dt2 = nowDate();
        return dt1.toString().equals(dt2.toString());
    }
}
