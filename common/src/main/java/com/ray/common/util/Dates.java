package com.ray.common.util;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class Dates {
    public static final SimpleDateFormat YMD_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy年M月d日");

    public static String yMdHms() {
        return YMD_HMS.format(new Date());
    }

    public static String yMdHms(Date date) {
        return YMD_HMS.format(date);
    }

    public static String yMd(Date date) {
        return YMD.format(date);
    }

    public static String formate(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date parse(String date) {
        try {
            return YMD_HMS.parse(date);
        } catch (ParseException e) {
        }
        return null;
    }


    public static boolean isToday(long when) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(when);

        int year = time.get(Calendar.YEAR);
        int dayOfYear = time.get(Calendar.DAY_OF_YEAR);

        time.setTimeInMillis(System.currentTimeMillis());
        return (year == time.get(Calendar.YEAR))
            && (dayOfYear == time.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isYesterday(long time) {
        Calendar thenCalendar = Calendar.getInstance();
        thenCalendar.setTimeInMillis(time);

        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance(); // 今天
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance(); // 昨天
        yesterday.add(Calendar.DATE, -1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        return thenCalendar.before(today) && thenCalendar.after(yesterday);
    }

    public static boolean isTomorrow(long time){
        Calendar thenCalendar = Calendar.getInstance();
        thenCalendar.setTimeInMillis(time);

        Calendar tomorrow = Calendar.getInstance(); // 明天
        tomorrow.add(Calendar.DATE, 1);
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);

        Calendar afterTomorrow = Calendar.getInstance(); // 后天
        afterTomorrow.add(Calendar.DATE, 2);
        afterTomorrow.set(Calendar.HOUR_OF_DAY, 0);
        afterTomorrow.set(Calendar.MINUTE, 0);
        afterTomorrow.set(Calendar.SECOND, 0);

        return thenCalendar.after(tomorrow) && thenCalendar.before(afterTomorrow);
    }

    public static boolean isAfterTomorrow(long time){
        Calendar thenCalendar = Calendar.getInstance();
        thenCalendar.setTimeInMillis(time);

        Calendar afterTomorrow = Calendar.getInstance(); // 后天
        afterTomorrow.add(Calendar.DATE, 2);
        afterTomorrow.set(Calendar.HOUR_OF_DAY, 0);
        afterTomorrow.set(Calendar.MINUTE, 0);
        afterTomorrow.set(Calendar.SECOND, 0);

        Calendar after3d = Calendar.getInstance(); // 大后天
        after3d.add(Calendar.DATE, 3);
        after3d.set(Calendar.HOUR_OF_DAY, 0);
        after3d.set(Calendar.MINUTE, 0);
        after3d.set(Calendar.SECOND, 0);

        return thenCalendar.after(afterTomorrow) && thenCalendar.before(after3d);
    }

    public static boolean isCurMonth(long when) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(when);

        int year = time.get(Calendar.YEAR);
        int month = time.get(Calendar.MONTH);

        time.setTimeInMillis(System.currentTimeMillis());
        return (year == time.get(Calendar.YEAR))
            && (month == time.get(Calendar.MONTH));
    }

    public static boolean isRecentThreeMonth(long when) {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.DAY_OF_MONTH, 1);
        time.set(Calendar.HOUR, 0);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);
        return isRangeMonth(when, time.getTimeInMillis(), 3, 0);
    }

    public static boolean isRangeMonth(long when, long target, int beginDiff, int afterDiff) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(target);
        time.add(Calendar.MONTH, -beginDiff);
        long startRange = time.getTimeInMillis();
        time.setTimeInMillis(target);
        time.add(Calendar.MONTH, afterDiff);
        long endRange = time.getTimeInMillis();

        return (when >= startRange)
            && (when < endRange);
    }

    public static boolean isCurYear(long when) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(when);

        int year = time.get(Calendar.YEAR);

        time.setTimeInMillis(System.currentTimeMillis());
        return year == time.get(Calendar.YEAR);
    }

    /**
     * 判断两个时间戳是不是在同一个星期（周一为界限）
     *
     * @return
     */
    public static boolean isInSameWeek(long timeStamp1, long timeStamp2) {
        long initTime = 316800000L;// "1970-01-05 00:00:00" 周一;
        boolean isSameWeek;
        if ((timeStamp1 - initTime) % (7 * 24 * 60 * 60 * 1000) == (timeStamp2 - initTime) % (7 * 24 * 60 * 60 * 1000)) {
            isSameWeek = true;
        } else {
            isSameWeek = false;
        }
        return isSameWeek;
    }

    public static boolean isOverYear(long when) {
        return System.currentTimeMillis() - when >= DateUtils.YEAR_IN_MILLIS;
    }

    public static int diffDays(long begin, long end) {
        return (int) ((end - begin) / 86400000);
    }

    public static int diffDaysNow(long begin) {
        return (int) ((System.currentTimeMillis() - begin) / 86400000);
    }

    /**
     * 根据出生日期，获取年龄
     *
     * @param birthTime 出生日期的time
     * @return 年龄(虚岁)
     */
    public static int getDateAge(long birthTime) {
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int curDayofMonth = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(birthTime);
        int birthYear = calendar.get(Calendar.YEAR);
        int birthMonth = calendar.get(Calendar.MONTH);
        int birthDayofMonth = calendar.get(Calendar.DAY_OF_MONTH);

        int age = curYear - birthYear;
        //不做月日(即周岁)判断
        return age;
    }

    /**
     * @return compCal时间大于targetCal，返回true，反之返回false
     */
    public static boolean after(Calendar targetCal, Calendar compCal) {
        return compCal.getTimeInMillis() > targetCal.getTimeInMillis();
    }

    /**
     * 计算月差
     *
     * @param targetCal 与之对比的calendar
     * @param compCal   待计算的calendar
     * @return compCal 时间大于 targetCal，返回正数，反之为负，如果同一个月，返回0
     */
    public static int getMonthOff(Calendar targetCal, Calendar compCal) {
        int targetY = targetCal.get(Calendar.YEAR);
        int compY = compCal.get(Calendar.YEAR);
        int targetM = targetCal.get(Calendar.MONTH);
        int compM = compCal.get(Calendar.MONTH);

        if (after(targetCal, compCal)) {
            compM += (compY - targetY) * 12;
        } else {
            targetM += (targetY - compY) * 12;
        }
        return compM - targetM;
    }

    public static long clearHMS(long when){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(when);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    public static void clearHMS(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}