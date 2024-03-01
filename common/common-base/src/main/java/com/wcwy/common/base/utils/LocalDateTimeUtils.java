package com.wcwy.common.base.utils;

/**
 * ClassName: LocalDateTimeUtils
 * Description:
 * date: 2023/10/6 15:08
 *
 * @author tangzhuo
 * @since JDK 1.8
 */

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * LocalDateTime工具类
 *
 * @author CL
 */
public class LocalDateTimeUtils {

    /**
     * 当前时间
     *
     * @return
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * Date 转 LocalDateTime
     *
     * @return
     */
    public static LocalDateTime convert(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime 转 Date
     *
     * @return
     */
    public static Date convert(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 今天开始时间
     *
     * @return
     */
    public static LocalDateTime todayStartTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    /**
     * 今天结束时间
     *
     * @return
     */
    public static LocalDateTime todayEndTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    /**
     * 昨天开始时间
     *
     * @return
     */
    public static LocalDateTime yesterdayStartTime() {
        return LocalDateTime.of(LocalDate.now().minus(1L, ChronoUnit.DAYS), LocalTime.MIN);
    }
    public static LocalDateTime yesterdayStartTime(LocalDate date) {
        return LocalDateTime.of(date.minus(1L, ChronoUnit.DAYS), LocalTime.MIN);
    }
    /**
     * 昨天结束时间
     *
     * @return
     */
    public static LocalDateTime yesterdayEndTime() {
        return LocalDateTime.of(LocalDate.now().minus(1L, ChronoUnit.DAYS), LocalTime.MAX);
    }
    public static LocalDateTime yesterdayEndTime(LocalDate date) {
        return LocalDateTime.of(date.minus(1L, ChronoUnit.DAYS), LocalTime.MAX);
    }
    /**
     * 最近7天开始时间
     *
     * @return
     */
    public static LocalDateTime last7DaysStartTime() {
        return LocalDateTime.of(LocalDate.now().minus(6L, ChronoUnit.DAYS), LocalTime.MIN);
    }

    /**
     * 最近7天结束时间
     *
     * @return
     */
    public static LocalDateTime last7DaysEndTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    /**
     * 最近30天开始时间
     *
     * @return
     */
    public static LocalDateTime last30DaysStartTime() {
        return LocalDateTime.of(LocalDate.now().minus(29L, ChronoUnit.DAYS), LocalTime.MIN);
    }

    /**
     * 最近30天结束时间
     *
     * @return
     */
    public static LocalDateTime last30DaysEndTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    /**
     * 最近一年开始时间
     *
     * @return
     */
    public static LocalDateTime last1YearStartTime() {
        return LocalDateTime.of(LocalDate.now().minus(1L, ChronoUnit.YEARS).plus(1L, ChronoUnit.DAYS), LocalTime.MIN);
    }

    /**
     * 最近一年结束时间
     *
     * @return
     */
    public static LocalDateTime last1YearEndTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    /**
     * 本周开始时间
     *
     * @return
     */
    public static LocalDateTime weekStartTime() {
        LocalDate now = LocalDate.now();
        return LocalDateTime.of(now.minusDays(now.getDayOfWeek().getValue() - 1), LocalTime.MIN);
    }

    public static LocalDate weekStartTime(LocalDate date) {
        LocalDate now = date;
        return LocalDate.from(LocalDateTime.of(now.minusDays(now.getDayOfWeek().getValue() - 1), LocalTime.MIN));
    }

    /**
     * 本周结束时间
     *
     * @return
     */
    public static LocalDateTime weekEndTime() {
        LocalDate now = LocalDate.now();
        return LocalDateTime.of(now.plusDays(7 - now.getDayOfWeek().getValue()), LocalTime.MAX);
    }
    public static LocalDateTime weekEndTime(LocalDate now) {

        return LocalDateTime.of(now.plusDays(7 - now.getDayOfWeek().getValue()), LocalTime.MAX);
    }
    /**
     * 本月开始时间
     *
     * @return
     */
    public static LocalDateTime monthStartTime() {
        return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
    }

    public static LocalDate monthStartTime(LocalDate date) {
        return LocalDate.from(LocalDateTime.of(date.with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN));
    }

    /**
     * 本月结束时间
     *
     * @return
     */
    public static LocalDateTime monthEndTime() {
        return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
    }

    public static LocalDate monthEndTime(LocalDate date) {
        return LocalDate.from(LocalDateTime.of(date.with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX));
    }

    /**
     * 本季度开始时间
     *
     * @return
     */
    public static LocalDateTime quarterStartTime() {
        LocalDate now = LocalDate.now();
        Month month = Month.of(now.getMonth().firstMonthOfQuarter().getValue());
        return LocalDateTime.of(LocalDate.of(now.getYear(), month, 1), LocalTime.MIN);
    }

    /**
     * 本季度结束时间
     *
     * @return
     */
    public static LocalDateTime quarterEndTime() {
        LocalDate now = LocalDate.now();
        Month month = Month.of(now.getMonth().firstMonthOfQuarter().getValue()).plus(2L);
        return LocalDateTime.of(LocalDate.of(now.getYear(), month, month.length(now.isLeapYear())), LocalTime.MAX);
    }

    /**
     * 本半年开始时间
     *
     * @return
     */
    public static LocalDateTime halfYearStartTime() {
        LocalDate now = LocalDate.now();
        Month month = (now.getMonthValue() > 6) ? Month.JULY : Month.JANUARY;
        return LocalDateTime.of(LocalDate.of(now.getYear(), month, 1), LocalTime.MIN);
    }

    /**
     * 本半年结束时间
     *
     * @return
     */
    public static LocalDateTime halfYearEndTime() {
        LocalDate now = LocalDate.now();
        Month month = (now.getMonthValue() > 6) ? Month.DECEMBER : Month.JUNE;
        return LocalDateTime.of(LocalDate.of(now.getYear(), month, month.length(now.isLeapYear())), LocalTime.MAX);
    }

    /**
     * 本年开始时间
     *
     * @return
     */
    public static LocalDateTime yearStartTime() {
        return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()), LocalTime.MIN);
    }

    /**
     * 本年结束时间
     *
     * @return
     */
    public static LocalDateTime yearEndTime() {
        return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.lastDayOfYear()), LocalTime.MAX);
    }

    /**
     * 上周开始时间
     *
     * @return
     */
    public static LocalDateTime lastWeekStartTime() {
        LocalDate lastWeek = LocalDate.now().minus(1L, ChronoUnit.WEEKS);
        return LocalDateTime.of(lastWeek.minusDays(lastWeek.getDayOfWeek().getValue() - 1), LocalTime.MIN);
    }

    public static LocalDate lastWeekStartTime(LocalDate date) {
        LocalDate lastWeek = date.minus(1L, ChronoUnit.WEEKS);
        return LocalDate.from(LocalDateTime.of(lastWeek.minusDays(lastWeek.getDayOfWeek().getValue() - 1), LocalTime.MIN));
    }

    /**
     * 上周结束时间
     *
     * @return
     */
    public static LocalDateTime lastWeekEndTime() {
        LocalDate lastWeek = LocalDate.now().minus(1L, ChronoUnit.WEEKS);
        return LocalDateTime.of(lastWeek.plusDays(7 - lastWeek.getDayOfWeek().getValue()), LocalTime.MAX);
    }

    public static LocalDate lastWeekEndTime(LocalDate date) {
        LocalDate lastWeek = date.minus(1L, ChronoUnit.WEEKS);
        return LocalDate.from(LocalDateTime.of(lastWeek.plusDays(7 - lastWeek.getDayOfWeek().getValue()), LocalTime.MAX));
    }

    /**
     * 上月开始时间
     *
     * @return
     */
    public static LocalDateTime lastMonthStartTime() {
        return LocalDateTime.of(LocalDate.now().minus(1L, ChronoUnit.MONTHS).with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
    }

    public static LocalDate lastMonthStartTime(LocalDate date) {
        return LocalDate.from(LocalDateTime.of(date.minus(1L, ChronoUnit.MONTHS).with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN));
    }

    /**
     * 上月结束时间
     *
     * @return
     */
    public static LocalDateTime lastMonthEndTime() {
        return LocalDateTime.of(LocalDate.now().minus(1L, ChronoUnit.MONTHS).with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
    }

    public static LocalDate lastMonthEndTime(LocalDate date) {
        return LocalDate.from(LocalDateTime.of(date.minus(1L, ChronoUnit.MONTHS).with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX));
    }

    /**
     * 上季度开始时间
     *
     * @return
     */
    public static LocalDateTime lastQuarterStartTime() {
        LocalDate now = LocalDate.now();
        Month firstMonthOfQuarter = Month.of(now.getMonth().firstMonthOfQuarter().getValue());
        Month firstMonthOfLastQuarter = firstMonthOfQuarter.minus(3L);
        int yearOfLastQuarter = firstMonthOfQuarter.getValue() < 4 ? now.getYear() - 1 : now.getYear();
        return LocalDateTime.of(LocalDate.of(yearOfLastQuarter, firstMonthOfLastQuarter, 1), LocalTime.MIN);
    }

    /**
     * 上季度结束时间
     *
     * @return
     */
    public static LocalDateTime lastQuarterEndTime() {
        LocalDate now = LocalDate.now();
        Month firstMonthOfQuarter = Month.of(now.getMonth().firstMonthOfQuarter().getValue());
        Month firstMonthOfLastQuarter = firstMonthOfQuarter.minus(1L);
        int yearOfLastQuarter = firstMonthOfQuarter.getValue() < 4 ? now.getYear() - 1 : now.getYear();
        return LocalDateTime.of(LocalDate.of(yearOfLastQuarter, firstMonthOfLastQuarter, firstMonthOfLastQuarter.maxLength()), LocalTime.MAX);
    }

    /**
     * 上半年开始时间
     *
     * @return
     */
    public static LocalDateTime lastHalfYearStartTime() {
        LocalDate now = LocalDate.now();
        int lastHalfYear = (now.getMonthValue() > 6) ? now.getYear() : now.getYear() - 1;
        Month firstMonthOfLastHalfYear = (now.getMonthValue() > 6) ? Month.JANUARY : Month.JULY;
        return LocalDateTime.of(LocalDate.of(lastHalfYear, firstMonthOfLastHalfYear, 1), LocalTime.MIN);
    }

    /**
     * 上半年结束时间
     *
     * @return
     */
    public static LocalDateTime lastHalfYearEndTime() {
        LocalDate now = LocalDate.now();
        int lastHalfYear = (now.getMonthValue() > 6) ? now.getYear() : now.getYear() - 1;
        Month lastMonthOfLastHalfYear = (now.getMonthValue() > 6) ? Month.JUNE : Month.DECEMBER;
        return LocalDateTime.of(LocalDate.of(lastHalfYear, lastMonthOfLastHalfYear, lastMonthOfLastHalfYear.maxLength()), LocalTime.MAX);
    }

    /**
     * 上一年开始时间
     *
     * @return
     */
    public static LocalDateTime lastYearStartTime() {
        return LocalDateTime.of(LocalDate.now().minus(1L, ChronoUnit.YEARS).with(TemporalAdjusters.firstDayOfYear()), LocalTime.MIN);
    }

    /**
     * 上一年结束时间
     *
     * @return
     */
    public static LocalDateTime lastYearEndTime() {
        return LocalDateTime.of(LocalDate.now().minus(1L, ChronoUnit.YEARS).with(TemporalAdjusters.lastDayOfYear()), LocalTime.MAX);
    }

    /**
     * 下周开始时间
     *
     * @return
     */
    public static LocalDateTime nextWeekStartTime() {
        LocalDate nextWeek = LocalDate.now().plus(1L, ChronoUnit.WEEKS);
        return LocalDateTime.of(nextWeek.minusDays(nextWeek.getDayOfWeek().getValue() - 1), LocalTime.MIN);
    }
    public static LocalDate nextWeekStartTime( LocalDate nextWeek1,int i ) {
        LocalDate nextWeek = nextWeek1.plus(i, ChronoUnit.WEEKS);
        return LocalDate.from(LocalDateTime.of(nextWeek.minusDays(nextWeek.getDayOfWeek().getValue() - 1), LocalTime.MIN));
    }
    /**
     * 下周结束时间
     *
     * @return
     */
    public static LocalDateTime nextWeekEndTime() {
        LocalDate nextWeek = LocalDate.now().plus(1L, ChronoUnit.WEEKS);
        return LocalDateTime.of(nextWeek.plusDays(7 - nextWeek.getDayOfWeek().getValue()), LocalTime.MAX);
    }
    public static LocalDate nextWeekEndTime( LocalDate nextWeek1,int i) {
        LocalDate nextWeek = nextWeek1.plus(i, ChronoUnit.WEEKS);
        return LocalDate.from(LocalDateTime.of(nextWeek.plusDays(7 - nextWeek.getDayOfWeek().getValue()), LocalTime.MAX));
    }

    /**
     * 下月开始时间
     *
     * @return
     */
    public static LocalDateTime nextMonthStartTime() {
        return LocalDateTime.of(LocalDate.now().plus(1L, ChronoUnit.MONTHS).with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
    }

    /**
     * 下月结束时间
     *
     * @return
     */
    public static LocalDateTime nextMonthEndTime() {
        return LocalDateTime.of(LocalDate.now().plus(1L, ChronoUnit.MONTHS).with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
    }

    /**
     * 下季度开始时间
     *
     * @return
     */
    public static LocalDateTime nextQuarterStartTime() {
        LocalDate now = LocalDate.now();
        Month firstMonthOfQuarter = Month.of(now.getMonth().firstMonthOfQuarter().getValue());
        Month firstMonthOfNextQuarter = firstMonthOfQuarter.plus(3L);
        int yearOfNextQuarter = firstMonthOfQuarter.getValue() > 9 ? now.getYear() + 1 : now.getYear();
        return LocalDateTime.of(LocalDate.of(yearOfNextQuarter, firstMonthOfNextQuarter, 1), LocalTime.MIN);
    }

    /**
     * 下季度结束时间
     *
     * @return
     */
    public static LocalDateTime nextQuarterEndTime() {
        LocalDate now = LocalDate.now();
        Month firstMonthOfQuarter = Month.of(now.getMonth().firstMonthOfQuarter().getValue());
        Month firstMonthOfNextQuarter = firstMonthOfQuarter.plus(5L);
        int yearOfNextQuarter = firstMonthOfQuarter.getValue() > 9 ? now.getYear() + 1 : now.getYear();
        return LocalDateTime.of(LocalDate.of(yearOfNextQuarter, firstMonthOfNextQuarter, firstMonthOfNextQuarter.maxLength()), LocalTime.MAX);
    }

    /**
     * 上半年开始时间
     *
     * @return
     */
    public static LocalDateTime nextHalfYearStartTime() {
        LocalDate now = LocalDate.now();
        int nextHalfYear = (now.getMonthValue() > 6) ? now.getYear() + 1 : now.getYear();
        Month firstMonthOfNextHalfYear = (now.getMonthValue() > 6) ? Month.JANUARY : Month.JULY;
        return LocalDateTime.of(LocalDate.of(nextHalfYear, firstMonthOfNextHalfYear, 1), LocalTime.MIN);
    }

    /**
     * 上半年结束时间
     *
     * @return
     */
    public static LocalDateTime nextHalfYearEndTime() {
        LocalDate now = LocalDate.now();
        int lastHalfYear = (now.getMonthValue() > 6) ? now.getYear() + 1 : now.getYear();
        Month lastMonthOfNextHalfYear = (now.getMonthValue() > 6) ? Month.JUNE : Month.DECEMBER;
        return LocalDateTime.of(LocalDate.of(lastHalfYear, lastMonthOfNextHalfYear, lastMonthOfNextHalfYear.maxLength()), LocalTime.MAX);
    }

    /**
     * 下一年开始时间
     *
     * @return
     */
    public static LocalDateTime nextYearStartTime() {
        return LocalDateTime.of(LocalDate.now().plus(1L, ChronoUnit.YEARS).with(TemporalAdjusters.firstDayOfYear()), LocalTime.MIN);
    }

    /**
     * 下一年结束时间
     *
     * @return
     */
    public static LocalDateTime nextYearEndTime() {
        return LocalDateTime.of(LocalDate.now().plus(1L, ChronoUnit.YEARS).with(TemporalAdjusters.lastDayOfYear()), LocalTime.MAX);
    }

    /**
     * @param createTime：比较时间 i:过去的天数 dayOneTime:比较时间的锚点
     * @return null
     * @Description: 比较两个日期是否是同一天
     * @Author tangzhuo
     * @CreateTime 2023/10/7 9:06
     */

    public static boolean isSameDay(LocalDateTime createTime, int i, LocalDate dayOneTime) throws ParseException {
        String toString = createTime.toString();
        String replace = toString.replace("T", " ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar rightNow = Calendar.getInstance();//当前时间
        String s = dayOneTime.toString();
        String replace1 = s.replace("T", " ");
        rightNow.setTime(dateFormat.parse(replace1));
        //日期的DATE -1 ：就是往前推1天；同理 +1 就是往后推1天
        rightNow.add(Calendar.DATE, i);//你要加减的日期
        Date dayOne = rightNow.getTime();
        Date parse = dateFormat.parse(replace);
        boolean flag = DateUtils.isSameDay(dayOne, parse);
        return flag;
    }

    public static boolean isSameDay(LocalDate createTime, int i, LocalDate dayOneTime) throws ParseException {
        String toString = createTime.toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar rightNow = Calendar.getInstance();//当前时间
        String s = dayOneTime.toString();
        String replace1 = s.replace("T", " ");
        rightNow.setTime(dateFormat.parse(replace1));
        //日期的DATE -1 ：就是往前推1天；同理 +1 就是往后推1天
        rightNow.add(Calendar.DATE, i);//你要加减的日期
        Date dayOne = rightNow.getTime();
        Date parse = dateFormat.parse(toString);
        boolean flag = DateUtils.isSameDay(dayOne, parse);
        return flag;
    }


    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static List<Map> getWeek(LocalDate date,int week) {
        List<Map> weekInfos = new ArrayList<>();
        for (int i = 0; i < week; i++) {
            Map weekInfo = new HashMap();

            weekInfo.put("start", nextWeekStartTime(date, i));
            weekInfo.put("end", nextWeekEndTime(date,i));
            weekInfo.put("order", i);
            weekInfos.add(weekInfo);
        }
        return weekInfos;
    }

    public static List<Map> getScope(String date) {
        //String date = "2023-10-01";
        String timeStrs[] = date.split("-");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(timeStrs[0]));
        c.set(Calendar.MONTH, Integer.parseInt(timeStrs[1]) - 1);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        int weeks = c.getActualMaximum(Calendar.WEEK_OF_MONTH);

        LocalDate localDateate = LocalDate.parse(date, dateTimeFormatter);
        //月份第一周的起始时间和结束时间
        LocalDate firstDay = localDateate.with(TemporalAdjusters.firstDayOfMonth());
        String firstDayStr = firstDay.format(dateTimeFormatter);
        String sunStr = getSunOfWeek(firstDayStr);

        List<Map> weekInfos = new ArrayList<>();
        for (int i = 1; i <= weeks; i++) {
            Map weekInfo = new HashMap();
            //第一周的起始时间就是当月的1号，结束时间就是周日
            if (i == 1) {
                weekInfo.put("start", firstDayStr);
                weekInfo.put("end", sunStr);
                weekInfo.put("order", i);
                //计算接下来每周的周一和周日
            } else if (i < weeks) {
                //由于sunStr是上一周的周日，所以取周一要取sunStr的下一周的周一
                String monDay = getLastMonOfWeek(sunStr);
                sunStr = getSunOfWeek(monDay);
                weekInfo.put("start", monDay);
                weekInfo.put("end", sunStr);
                weekInfo.put("order", i);
                //由于最后一周可能结束时间不是周日，所以要单独处理
            } else {
                String monDay = getLastMonOfWeek(sunStr);
                //结束时间肯定就是当前月的最后一天
                LocalDate lastDay = localDateate.with(TemporalAdjusters.lastDayOfMonth());
                String endDay = lastDay.format(dateTimeFormatter);
                weekInfo.put("start", monDay);
                weekInfo.put("end", endDay);
                weekInfo.put("order", i);
            }

            weekInfos.add(weekInfo);

        }
        return weekInfos;
    }


    //算出所在周的周日
    public static String getSunOfWeek(String time) {
        LocalDate now = LocalDate.parse(time, dateTimeFormatter);
        // LocalDate endday = localDateate.with(TemporalAdjusters.next(java.time.DayOfWeek.MONDAY)).minusDays(1);
        //LocalDateTime.of(now.minusDays(now.getDayOfWeek().getValue() - 1), LocalTime.MIN);

        LocalDateTime of = LocalDateTime.of(now.plusDays(7 - now.getDayOfWeek().getValue()), LocalTime.MAX);
        String endDayStr = of.format(dateTimeFormatter);
        return endDayStr;
    }

    //下一周的周一
    public static String getLastMonOfWeek(String time) {
        //nextWeekStartTime1(time);
        LocalDate localDateate = LocalDate.parse(time, dateTimeFormatter);
        LocalDate nextWeek = localDateate.plus(1L, ChronoUnit.WEEKS);
        LocalDate date = nextWeek.minusDays(nextWeek.getDayOfWeek().getValue() - 1);
        LocalDateTime of = LocalDateTime.of(date, LocalTime.MIN);
        return of.format(dateTimeFormatter);
    }

    /**
     * 获取下第几周
     *
     * @param
     * @param i
     * @return
     */
    public static LocalDate getLastMonOfWeek(LocalDate localDateate, long i) {
        //nextWeekStartTime1(time);
        LocalDate nextWeek = localDateate.plus(i, ChronoUnit.WEEKS);
        LocalDate date = nextWeek.minusDays(nextWeek.getDayOfWeek().getValue() - 1);
        return date;
    }

    public static void main(String[] args) {
        System.out.println("当前时间：" + now());
        System.out.println("Date 转 LocalDateTime：" + convert(new Date()));
        System.out.println("LocalDateTime 转 Date：" + convert(LocalDateTime.now()));
        System.out.println("今天开始时间：" + todayStartTime());
        System.out.println("今天结束时间：" + todayEndTime());
        System.out.println("昨天开始时间：" + yesterdayStartTime());
        System.out.println("昨天结束时间：" + yesterdayEndTime());
        System.out.println("最近7天开始时间：" + last7DaysStartTime());
        System.out.println("最近7天结束时间：" + last7DaysEndTime());
        System.out.println("最近30天开始时间：" + last30DaysStartTime());
        System.out.println("最近30天天结束时间：" + last30DaysEndTime());
        System.out.println("最近一年开始时间：" + last1YearStartTime());
        System.out.println("最近一年结束时间：" + last1YearEndTime());
        System.out.println("本周开始时间：" + weekStartTime());
        System.out.println("本周结束时间：" + weekEndTime());
        System.out.println("本月开始时间：" + monthStartTime());
        System.out.println("本月结束时间：" + monthEndTime());
        System.out.println("本季度开始时间：" + quarterStartTime());
        System.out.println("本季度结束时间：" + quarterEndTime());
        System.out.println("本半年开始时间：" + halfYearStartTime());
        System.out.println("本半年结束时间：" + halfYearEndTime());
        System.out.println("本年开始时间：" + yearStartTime());
        System.out.println("本年结束时间：" + yearEndTime());
        System.out.println("上周开始时间：" + lastWeekStartTime());
        System.out.println("上周结束时间：" + lastWeekEndTime());
        System.out.println("上月开始时间：" + lastMonthStartTime());
        System.out.println("上月结束时间：" + lastMonthEndTime());
        System.out.println("上季度开始时间：" + lastQuarterStartTime());
        System.out.println("上季度结束时间：" + lastQuarterEndTime());
        System.out.println("上半年开始时间：" + lastHalfYearStartTime());
        System.out.println("上半年结束时间：" + lastHalfYearEndTime());
        System.out.println("上一年开始时间：" + lastYearStartTime());
        System.out.println("上一年结束时间：" + lastYearEndTime());
        System.out.println("下周开始时间：" + nextWeekStartTime());
        System.out.println("下周结束时间：" + nextWeekEndTime());
        System.out.println("下月开始时间：" + nextMonthStartTime());
        System.out.println("下月结束时间：" + nextMonthEndTime());
        System.out.println("下季度开始时间：" + nextQuarterStartTime());
        System.out.println("下季度结束时间：" + nextQuarterEndTime());
        System.out.println("下半年开始时间：" + nextHalfYearStartTime());
        System.out.println("下半年结束时间：" + nextHalfYearEndTime());
        System.out.println("下一年开始时间：" + nextYearStartTime());
        System.out.println("下一年结束时间：" + nextYearEndTime());
    }

    public static List<Map> getMonthScope(LocalDate beginDate, LocalDate endDate) {
        List<Map> weekInfos = new ArrayList<>();
        long between = ChronoUnit.MONTHS.between(beginDate, endDate) + 1;

        for (int i = 0; i < between; i++) {
            Map weekInfo = new HashMap();
            LocalDate A = LocalDate.from(LocalDateTime.of(beginDate.plus(i, ChronoUnit.MONTHS).with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN));
            LocalDate B = LocalDate.from(LocalDateTime.of(A.with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX));
            weekInfo.put("start", A);
            weekInfo.put("end", B);
            weekInfo.put("order", i + 1);
            weekInfos.add(weekInfo);
        }

        return weekInfos;
    }
}
