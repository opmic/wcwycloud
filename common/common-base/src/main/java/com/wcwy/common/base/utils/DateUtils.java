package com.wcwy.common.base.utils;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 日期工具类
 *
 * @author E20151109
 */
public final class DateUtils {
    private DateUtils() {
    }

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_START = "yyyy-MM-dd 00:00:00";
    public static final String DATE_TIME_FORMAT_END = "yyyy-MM-dd 23:59:59";

    public static final String DATE_TIME_FORMAT_BACK = "yyyy/MM/dd HH:mm:ss";

    public static final String DATE_TIME_FORMAT_BACK_YYYY_MM_DD_HH_MM = "yyyy/MM/dd HH:mm";

    public static final String DATE_PATTERN_YYYY_MM = "yyyy-MM";
    public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_YYYY_MM_DD_HH_MM_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String DATE_PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String DATE_PATTERN_YYYYMMDD_HHMMSS = "yyyyMMdd-HHmmss";
    public static final String DATE_PATTERN_YYYYMMDDHHMM = "yyyyMMddHHmm";

    public static final String DATE_PATTERN_YYYYMMDDHHMMSS_SSS = "yyyyMMddHHmmSSS";

    public static final String DATE_PARENT_YYMMDDHHMMSS_SSS = "yyMMddHHmmSSS";
    public static final String DATE_PARENT_YYMMDDHHMM = "yyMMddHHmm";
    public static final String DATE_PATTERN_YYYYMMDDHH = "yyyyMMddHH";

    public static final String DATE_PATTERN_YYMMDDHH = "yyMMddHH";
    public static final String DATE_PATTERN_YYYYMMDD = "yyyyMMdd";

    public static final String DATE_YYMMDDHHSSSSS = "yyMMddHH";


    public static final String TIME_PATTERN_HHMMSS = "HHmmSS";

    public static final String MONGTH_PATTERN_YYYYMM = "yyyyMM";

    public static final String DATE_PATTERN_YYMMDDHHMMSS = "yyMMddHHmmss";

    public static final String DATE_PATTERN_YYYY = "yyyy";
    public static final String DATE_PATTERN_MMdd = "MMdd";
    public static final String DATE_PATTERN_MM = "MM";

    public static final String DATE_PATTERN_CHINESE_YYYY_MM_DD = "yyyy年M月d日";


    public static final String DATE_PATTERN_CHINESE_MM_DD = "M月d日";
    public static final String DATE_PATTERN_MM_DD = "MM-dd";

    public static final String DATE_PATTERN_CHINESE_YYYY_MM_DD_HH_MM = "yyyy年M月d日 HH:mm";

    public static final String DATE_TIME_FORMAT_SLASH_MM_DD_HH_MM = "MM/dd HH:mm";
    public static final String DATE_DD_HH_MM = "HH:mm:ss";
    public static final String DATE_HH_MM = "HH:mm";
    public static final String DATE_HH = "HH";

    //2018-06-08T10:34:56+08:00
    //为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
    public static final String WECHAT_DATE = "yyyMMddHHmmss";

    /**
     * 获取当前时间:默认格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentDateStr() {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return currentDate.format(dateFormat);
    }

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static final String getCurrentDateStr(String format) {
        if (StringUtils.isEmpty(format)) {
            return getCurrentDateStr();
        }
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        return currentDate.format(dateFormat);
    }

    /**
     * @return 返回String类型
     * @Title:获取当天00:00:00
     * @Description:
     * @author lirihui
     * @date 2022年3月19日 下午4:06:58
     */
    public static String getCurrentStartDateStr() {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_START);
        return currentDate.format(dateFormat);
    }

    /**
     * @return 返回Date类型
     * @Title:获取当天00:00:00
     * @Description:
     * @author lirihui
     * @date 2022年3月19日 下午4:06:58
     */
    public static Date getCurrentStartDateDate() {
        Date currentDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_START);
            currentDate = simpleDateFormat.parse(getCurrentStartDateStr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    /**
     * @return 返回String类型
     * @Title:获取当天23:59:59
     * @Description:
     * @author lirihui
     * @date 2022年3月19日 下午4:06:58
     */
    public static String getCurrentEndDateStr() {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_END);

        return currentDate.format(dateFormat);
    }

    /**
     * @return 返回Date类型
     * @Title:获取当天23:59:59
     * @Description:
     * @author lirihui
     * @date 2022年3月19日 下午4:06:58
     */
    public static Date getCurrentEndDateDate() {
        Date currentDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_END);
            currentDate = simpleDateFormat.parse(getCurrentEndDateStr());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    /**
     * 如20180901
     *
     * @return
     */
    public static String getYYYYMMdd() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_YYYYMMDD);
        return sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * 过去多少时间
     *
     * @param time
     * @return
     */
    public static String getlastTimes(Date time) {


        return getChineseTime(System.currentTimeMillis() - time.getTime());
    }

    public static String getChineseTime(long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        }
        if (minute > 0) {
            sb.append(minute + "分");
        }
        if (second > 0) {
            sb.append(second + "秒");
        }
		/*if(milliSecond > 0) {
			sb.append(milliSecond+"毫秒");
		}*/
        sb.append("前");
        return sb.toString();

    }


    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static final String getDateStr(Date inDate, String format) {
        if (StringUtils.isEmpty(format)) {
            return getCurrentDateStr();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String outDate = dateFormat.format(inDate);
        return outDate;
    }


    /**
     * 格式化时间
     *
     * @param source
     * @return
     */
    public static final String formatDate(Date source) {
        return formatDate(source, null);
    }

    /**
     * 格式化时间
     *
     * @param source
     * @param format
     * @return
     */
    public static final String formatDate(Date source, String format) {
        if (source == null) {
            return "";
        }
        DateFormat dateFormat = null;
        if (StringUtils.isEmpty(format)) {
            dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        } else {
            dateFormat = new SimpleDateFormat(format);
        }
        return dateFormat.format(source);
    }

    /**
     * 解析日期字符串.默认个格式
     * 默认是按照yyyy-MM-dd HH:mm:ss解析
     *
     * @param source
     * @return
     */
    public static final Date parseDateStr(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 解析时间字符串：默认是按照yyyy-MM-dd HH:mm:ss解析
     *
     * @param source
     * @param format
     * @return
     */
    public static final Date parseDateStr(String source, String format) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        DateFormat dateFormat = null;
        if (StringUtils.isEmpty(format)) {
            dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        } else {
            dateFormat = new SimpleDateFormat(format);
        }
        try {
            source = source.replace(" ", " ");
            return dateFormat.parse(source);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 查询今天的前/后几天的整日日期
     * <p/>
     * 比如2015-12-18日的前一天--2015-12-17
     * <p/>
     * 比如2015-12-18日的后一天--2015-12-19
     *
     * @param day
     * @return
     */
    public static String getDayBeforeOrAfter(Integer day) {
        // 今天整天日期
        Date currDt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_YYYY_MM_DD);
        String currDtStr = sdf.format(currDt.getTime());
        Date currDay = null;
        try {
            currDay = sdf.parse(currDtStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(currDay.getTime()));
        calendar.add(Calendar.DATE, day);
        Date target = calendar.getTime();
        return sdf.format(target);
    }

    /**
     * 查询某天的前/后几天的整日日期
     * <p/>
     * 比如2015-12-18日的前一天--2015-12-17
     * <p/>
     * 比如2015-12-18日的后一天--2015-12-19
     *
     * @param day
     * @return
     */
    public static String getDayBeforeOrAfter(Date sourceDt, Integer day) {
        // 今天整天日期
        if (sourceDt == null) {
            sourceDt = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_YYYY_MM_DD);
        String sourceDtStr = sdf.format(sourceDt.getTime());
        Date sourceDay = null;
        try {
            sourceDay = sdf.parse(sourceDtStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(sourceDay.getTime()));
        calendar.add(Calendar.DATE, day);
        Date target = calendar.getTime();
        return sdf.format(target);
    }

    /**
     * 获得指定日期，偏移指定时间
     *
     * @param inDate 指定时间
     *               deviationNum 偏移值（<0:向左偏移, >0：向右偏移）
     *               偏移类型（年：Y/y, 月：M/m， 日：D/d, 时：H/h， 分：MIN/min，秒：S/s）
     * @return
     */
    public static Date getDayBeforeOrAfter(Date inDate, Integer changeNum, String changeType) {
        // 今天整天日期
        if (inDate == null) {
            inDate = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        String sourceDtStr = sdf.format(inDate.getTime());
        Date sourceDay = null;
        try {
            sourceDay = sdf.parse(sourceDtStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(sourceDay.getTime()));
        if ("Y".equals(changeType.toUpperCase())) {
            calendar.add(Calendar.YEAR, changeNum);
        } else if ("M".equals(changeType.toUpperCase())) {
            calendar.add(Calendar.MONTH, changeNum);
        } else if ("D".equals(changeType.toUpperCase())) {
            calendar.add(Calendar.DATE, changeNum);
        } else if ("H".equals(changeType.toUpperCase())) {
            calendar.add(Calendar.HOUR_OF_DAY, changeNum);
        } else if ("MIN".equals(changeType.toUpperCase())) {
            calendar.add(Calendar.MINUTE, changeNum);
        } else if ("S".equals(changeType.toUpperCase())) {
            calendar.add(Calendar.SECOND, changeNum);
        }
        Date outDate = calendar.getTime();
        return outDate;
        //return sdf.format(target);
    }

    /**
     * 获取当前秒数:从 1970年至今
     *
     * @return
     */
    public static int getTimeSeconds() {
        long timeMillis = System.currentTimeMillis();
        long timeSeconds = timeMillis / 1000;
        return (int) timeSeconds;
    }

    //获取本周的开始时间
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取本周的结束时间
     */
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    /**
     * 获取本月的开始时间
     *
     * @return
     */
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取本月的结束时间
     *
     * @return
     */
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());

    }

    /**
     * 获取今年是哪一年
     *
     * @return
     */
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    /**
     * 获取本月是哪一月
     *
     * @return
     */
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    /**
     * @return
     * @Description: 获取当前时间为一天的那个小时
     * @Title: getNowHour
     * @author linjia
     * @date 2021年2月25日
     */
    public static int getNowHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * @param d:时间，如果为空，则为当前时间
     * @return
     * @Description: 获取指定时间为一天的那个小时
     * @Title: getNowHour
     * @author linjia
     * @date 2021年2月25日
     */
    public static int getDateHour(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取某个日期的开始时间
     *
     * @param d
     * @return
     */
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取某个日期的结束时间
     *
     * @param d
     * @return
     */
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Date getTodayEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    public static Date getTodayBeginTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 0);
        todayEnd.set(Calendar.MINUTE, 0);
        todayEnd.set(Calendar.SECOND, 0);
        todayEnd.set(Calendar.MILLISECOND, 0);
        return todayEnd.getTime();
    }

    public static Date getTodayBeginTime(Date d) {
        Calendar todayEnd = Calendar.getInstance();
        if (null != d) {
            todayEnd.setTime(d);
        }
        todayEnd.set(Calendar.HOUR_OF_DAY, 0);
        todayEnd.set(Calendar.MINUTE, 0);
        todayEnd.set(Calendar.SECOND, 0);
        todayEnd.set(Calendar.MILLISECOND, 0);
        return todayEnd.getTime();
    }


    public static Timestamp getbeforeTimeByMinute(Date d, Integer minute) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.add(Calendar.MINUTE, -minute);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static String formatTimeStamp(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date date = new Date(timeStamp);
        return sdf.format(date);
    }


    public static String tranToChineseTime(Integer timeLen) {
        int hour = timeLen / 3600;
        int minute = timeLen % 3600 / 60;
        int second = timeLen % 3600 % 60;
        return ((hour == 0 ? "" : hour + "时") + (minute == 0 ? "" : minute + "分") + second + "秒");
    }


    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间（null 则默认new Date()）
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回代号（-1：开始时间之前，0：在区间中，1：结束时间之后）
     */
    public static int isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime == null) {
            nowTime = new Date();
        }
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return 0;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.before(begin)) {//date.after(end)
            return -1;
        } else if (date.after(begin) && date.before(end)) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            //	System.out.println("判断day2 - day1 : " + (day2-day1));
            return day2 - day1;
        }
    }

    /**
     * @return 当前时间
     * @Description:获取当前时间
     * @Title: getDate
     * @author linjia
     * @date 2020年3月5日
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * @return
     * @Description: 获取当前时间的时间戳
     * @Title: getTime
     * @author linjia
     * @date 2020年11月27日
     */
    public static Long getTime() {
        return System.currentTimeMillis();
    }

    /**
     * @param date：可以为空，空则获取当前时间
     * @return 秒级别时间戳
     * @Description: 获取秒级别时间戳
     * @Title: getSecondTimestampTwo
     * @author linjia
     * @date 2021年4月1日
     */
    public static Long getSecondTimestampTwo(Date date) {
        if (null == date) {
            return Long.valueOf(String.valueOf(System.currentTimeMillis() / 1000));
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return Long.valueOf(timestamp);
    }

    /**
     * @param d1：如果为空取当前时间
     * @param d2：如果为空取当前时间
     * @return d1-d2=间隔时间（秒）
     * @Description: 比较两个时间，间隔多少秒
     * @Title: getDateDifference
     * @author linjia
     * @date 2021年3月4日
     */
    public static Long getDateDifference(Date d1, Date d2) {
        if (null == d1 && null == d2) {
            return 0L;
        }
        Long l1 = null == d1 ? getTime() : d1.getTime();
        Long l2 = null == d2 ? getTime() : d2.getTime();
        return l1 - l2;
    }

    /**
     * @param hour：时分秒，格式：18:18:18-->18点18分18秒
     * @return 今天的日期加传入的时间，如果传入的时间格式错误，则返回null
     * @Description: 设置一个新的时间，通过传入的时分秒
     * @Title: setNewDateHour
     * @author linjia
     * @date 2020年3月2日
     */
    public static Date setNewDateHour(String hour) {
        if (StringUtils.isEmpty(hour)) {
            return null;
        }
        String[] split = hour.split(":");
        if (3 != split.length) {//切割出来不是时分秒则返回null

        }
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
        instance.set(Calendar.MINUTE, Integer.parseInt(split[1]));
        instance.set(Calendar.SECOND, Integer.parseInt(split[2]));
        return instance.getTime();
    }

    /**
     * @param date：可以为null，null为当前时间
     * @return 增加N月份的时间
     * @Description: 指定一个时间，增加指定月份（可以为负数，负数得到过去时间）
     * @Title: addMonth
     * @author linjia
     * @date 2020年3月5日
     */
    public static Date addMonth(Date date, int day) {
        Calendar instance = Calendar.getInstance();
        if (null != date) {
            instance.setTime(date);
        }
        instance.add(Calendar.MONTH, day);
        return instance.getTime();
    }

    /**
     * @param date：可以为null，null为当前时间
     * @return 增加N小时的时间
     * @Description: 指定一个时间，增加指定小时（可以为负数，负数得到过去时间）
     * @Title: addHour
     * @author linjia
     * @date 2020年3月5日
     */
    public static Date addDay(Date date, int day) {
        Calendar instance = Calendar.getInstance();
        if (null != date) {
            instance.setTime(date);
        }
        instance.add(Calendar.DAY_OF_MONTH, day);
        return instance.getTime();
    }

    /**
     * @param date：可以为null，null为当前时间
     * @param hour：可以为负数，负数得到过去时间
     * @return 增加N小时的时间
     * @Description: 指定一个时间，增加指定小时（可以为负数，负数得到过去时间）
     * @Title: addHour
     * @author linjia
     * @date 2020年3月5日
     */
    public static Date addHour(Date date, int hour) {
        Calendar instance = Calendar.getInstance();
        if (null != date) {
            instance.setTime(date);
        }
        instance.add(Calendar.HOUR_OF_DAY, hour);
        return instance.getTime();
    }

    /**
     * @param date：可以为null，null为当前时间
     * @param minute：可以为负数，负数得到过去时间
     * @return 增加N分钟的时间
     * @Description: 指定一个时间，增加指定分钟（可以为负数，负数得到过去时间）
     * @Title: addMinute
     * @author linjia
     * @date 2020年3月5日
     */
    public static Date addMinute(Date date, int minute) {
        Calendar instance = Calendar.getInstance();
        if (null != date) {
            instance.setTime(date);
        }
        instance.add(Calendar.MINUTE, minute);
        return instance.getTime();
    }

    /**
     * @param second：可以为负数，负数得到过去时间
     * @return 增加N秒的时间
     * @Description: 当前时间，增加指定秒（可以为负数，负数得到过去时间）
     * @Title: addSecond
     * @author linjia
     * @date 2020年6月23日
     */
    public static Date addSecond(Date date, int second) {
        if (0 == second) {
            return getDate();
        }
        Calendar instance = Calendar.getInstance();
        if (null != date) {
            instance.setTime(date);
        }
        instance.add(Calendar.SECOND, second);
        return instance.getTime();
    }

    /**
     * @param second：可以为负数，负数得到过去时间
     * @return 增加N秒的时间
     * @Description: 当前时间，增加指定秒（可以为负数，负数得到过去时间）
     * @Title: addSecond
     * @author linjia
     * @date 2020年6月23日
     */
    public static Long addSecond(Long second) {
        long currentTimeMillis = System.currentTimeMillis();
        if (null == second) {
            return currentTimeMillis;
        }
        return currentTimeMillis + (second * 1000);
    }


    /**
     * 获取定时任务执行的时间
     * 如果是在0-8点 ,则返回今天八点
     * 如果超过8点，则返回第二天八点
     */
    public static Date getTaskDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        System.out.println("0点时间为 " + formatDate(zero));
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        Date eightHour = calendar.getTime();
        System.out.println("8点时间为 " + formatDate(eightHour));
        if (isEffectiveDate(getDate(), zero, eightHour) == 0) {
            //时间区域内
            return eightHour;
        }
        return getNextDay8Hour();
    }

    //返回第二天八点时间
    public static Date getNextDay8Hour() {
        Date date = new Date();
        date.setDate(date.getDate() + 1);
        date.setHours(8);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }

    /**
     * 返回时间的年月
     *
     * @param date 时间
     * @return 2020-10
     */
    public static String getYYYYMM(Date date) {
        String dateString = formatDate(date);
        return dateString.substring(0, 7);
    }

    public static String getYYYYMMDD(Date date) {
        String dateString = formatDate(date);
        return dateString.substring(0, 10);
    }

    /**
     * 判断两天是否相等
     *
     * @param date1 时间一
     * @param date2 时间2
     * @return true 相等
     * @author liming
     * @date 11:25 2020/6/2
     */
    public static boolean isSameDay(Date date1, Date date2) {
        String data1Str = formatDate(date1, DATE_PATTERN_YYYY_MM_DD);
        String data2Str = formatDate(date2, DATE_PATTERN_YYYY_MM_DD);
        return data1Str.equals(data2Str);
    }

    /**
     * 获取时间差开始时间
     *
     * @param date
     * @param day  想要获取的日期与传入日期的差值 比如想要获取传入日期前四天的日期 day=-4即可
     * @return
     */
    public static String getLongBeginDay(Date date, int day) {
        return formatDate(getSomeDay(date, day), DATE_PATTERN_YYYY_MM_DD) + " " + "00:00:00";
    }

    /**
     * @param date
     * @param day  想要获取的日期与传入日期的差值 比如想要获取传入日期前四天的日期 day=-4即可
     * @return
     */
    public static Date getSomeDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 获取最后时间
     *
     * @param date
     * @return
     */
    public static String getLongEndDay(Date date) {
        return formatDate(date, DATE_PATTERN_YYYY_MM_DD) + " " + "23:59:59";
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos);
    }

    /**
     * 获取中国时间
     *
     * @return
     */
    public static Date getChinaDate() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        Date chinadate = calendar.getTime();
        return chinadate;
    }

    /**
     * @param d：需要设置的时间，如果为空，则用当前时间
     * @param h：需要设置的小时，如果为空，则不设置
     * @param m：需要设置的分钟，如果为空，则不设置
     * @param s：需要设置的秒钟，如果为空，则不设置
     * @return Date：设置后的时间
     * @Description: 设置传入时间的时分秒，如果时间为空，获取当前时间
     * @Title: setHour
     * @author linjia
     * @date 2021年7月29日
     */
    public static Date setHour(Date d, Integer h, Integer m, Integer s) {
        Calendar instance = Calendar.getInstance();
        if (null != d) {
            instance.setTime(d);
        }
        if (null != h) {
            instance.set(Calendar.HOUR_OF_DAY, h);
        }
        if (null != m) {
            instance.set(Calendar.MINUTE, m);
        }
        if (null != s) {
            instance.set(Calendar.SECOND, s);
        }
        return instance.getTime();
    }

    /**
     * 判断两个日期间是否超过的年数
     *
     * @param time1
     * @param time2
     * @param numYear
     * @return
     */
    public static Boolean DateCompare(Date time1, Date time2, int numYear) {
        Date time3 = add(time1, Calendar.YEAR, numYear);
        if (time3.getTime() < time2.getTime()) {
            return true;
        }
        return false;
    }

    /**
     * 时间加减
     *
     * @param date
     * @param calendarField ：Calendar.YEAR/ Calendar.MONTH /Calendar.DAY
     * @param amount
     * @return
     */
    public static Date add(final Date date, final int calendarField, final int amount) {
        if (date == null) {
            return null;
        }
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();

    }

    /**
     * @param date
     * @return
     * @Description:
     * @Title: asLocalDate
     * @author linjia
     * @date 2020年7月3日
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        if (null == date) {
            date = new Date();
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    /**
     * @Description: 时间字符串排序
     * @Title: sortList
     * @param list
     * @return
     * @author wnc
     * @date 2021年11月8日
     */


    /**
     * @param
     * @return java.util.Date
     * @author mozhenqiang
     * @description 获取今天最早的时间
     * @date 17:28 2022/1/17
     **/
    public static Date todayEarliest() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }

    /**
     * @param
     * @return java.util.Date
     * @author mozhenqiang
     * @description 获取今天最后的时间
     * @date 17:28 2022/1/17
     **/
    public static Date todayLast() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date zero = calendar.getTime();
        return zero;
    }

    /**
     * @Description: 获取指定日期的最后时间
     * @Author: duanchao
     * @Date: 2022/3/29 10:22
     * @param: date
     * @return: java.util.Date
     */
    public static Date lastDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date zero = calendar.getTime();
        return zero;
    }

    /**
     * @param
     * @return java.lang.String
     * @author mozhenqiang
     * @description 获取微信订单失效时间，订单失效时间为10分钟
     * @date 14:38 2022/3/1
     **/
    public static String wechatOrderExpireDate() {
        return formatDate(addMinute(new Date(), 2), WECHAT_DATE);
    }


    /**
     * @param avgAssignedDuration
     * @return
     * @Title:根据传入的秒数，转换成时分秒
     * @Description:
     * @author lirihui
     * @date 2022年3月29日 下午6:12:11
     */
    public static String changeTimeStrBySecond(Integer avgAssignedDuration) {
        Integer seconds = avgAssignedDuration;
        Integer temp = 0;
        StringBuffer sb = new StringBuffer();
        temp = seconds / 86400;
        if (temp > 0) {
            sb.append(temp).append("天");
        }
        temp = seconds % 86400 / 3600;
        if (temp > 0) {
            sb.append(temp).append("时");
        } else if (sb.length() > 0) {
            sb.append(0).append("时");
        }
        temp = seconds % 86400 % 3600 / 60;
        if (temp > 0) {
            sb.append(temp).append("分");
        } else if (sb.length() > 0) {
            sb.append(0).append("分");
        }
        temp = seconds % 86400 % 3600 % 60;
        if (temp > 0) {
            sb.append(temp).append("秒");
        } else if (sb.length() > 0) {
            sb.append(0).append("秒");
        }
        return sb.toString();
    }

    /**
     * @return 返回Date类型
     * @Title:获取当天00:00:00
     * @Description:
     * @author lirihui
     * @date 2022年3月19日 下午4:06:58
     */
    public static Date getDayStartDateDate(String date) {
        Date currentDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_START);
            currentDate = simpleDateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    /**
     * 获取startDate日期后month月的日期
     *
     * @param startDate 开始日期
     * @param month     几个月后
     * @return
     */
    public static Date getMonthDate(Date startDate, int month) {
        LocalDateTime localDateTime = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().plusMonths(month);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    /**
     * 获LocalDateTime转换为Date
     */
    public static Date fromLocalDateTime(LocalDateTime ldt) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = ldt;
        ZonedDateTime zdt = localDateTime.atZone(zoneId);

        Date date = Date.from(zdt.toInstant());
        return date;
    }
    /**
     * @Author tangzhuo
     * @Description //当前时间进行扩十
     * @Date 9:05 2022/6/22
     * @Param
     * @return
     **/

/*    public static  Date addDate(Date rawDate,Date dilatationDate){

        Date buydate = new Date();//设置购买时间

        long time =buydate.getTime();//设置套餐到期时间

        time = time + 31*1*24*60*60*1000L; //两个日期型相加，一定要用long类型，运算时，数值后要加“L”

        Date expirtime = new Date();

        expirtime.setTime(time);
        return
    }*/

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - past);
        Date today = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String result = sdf.format(today);
        return result;
    }


    /**
     * @return
     * @description: 一秒钟前
     * @author: Jeff
     * @date: 2019年12月20日
     */
    private static Date getTime1() {


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -1);
        return cal.getTime();
    }

    /**
     * @return
     * @description: 一分钟前
     * @author: Jeff
     * @date: 2019年12月20日
     */
    private static Date getTime2() {


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -1);
        return cal.getTime();
    }

    /**
     * @return
     * @description: 二分钟前
     * @author: Jeff
     * @date: 2019年12月20日
     */
    public static Date getTimeTwo() {


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -2);
        return cal.getTime();
    }

    /**
     * @return
     * @description: 一小时前
     * @author: Jeff
     * @date: 2019年12月20日
     */
    private static Date getTime3() {


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        return cal.getTime();
    }

    /**
     * @return
     * @description: 一天前
     * @author: Jeff
     * @date: 2019年12月20日
     */
    private static Date getTime4() {


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * @return
     * @description: 一周前
     * @author: Jeff
     * @date: 2019年12月20日
     */
    private static Date getTime5() {


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        return cal.getTime();
    }

    /**
     * @return
     * @description: 一月前
     * @author: Jeff
     * @date: 2019年12月20日
     */
    private static Date getTime6() {


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    /**
     * @return
     * @description: 获取几个月前
     * @author: Jeff
     * @date: month:月份
     */
    public static Date getTimeMonth(int month) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }


    /**
     * @return
     * @description: 一年前
     * @author: Jeff
     * @date: 2019年12月20日
     */
    public static Date getTime7() {


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    //获取过去的年份
    public static Date getTime7(Integer i) {


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -i);
        return cal.getTime();
    }

    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String result = format.format(today);
        return result + "Z";
    }


    /**
     * 获取今天的凌晨时间
     */


    public static String weeHours() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date beginOfDate = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(beginOfDate);
    }


    /**
     * 校验日期区间时间跨度是否在一年之内
     * 参数日期格式应为 yyyy-MM-dd，例如：2020-12-31
     *
     * @param beginDate 开始日期
     * @param dueDate   结束日期
     */
    public static boolean checkIsOneYear(String beginDate, String dueDate) {
        //365天的毫秒数
        long ms = 31536000000L;
        try {
            //规定需要转换的日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //文本转日期
            Date due = sdf.parse(dueDate);
            Date begin = sdf.parse(beginDate);
            long time = due.getTime() - begin.getTime();
            System.out.println(time + "=" + due.getTime() + "-" + begin.getTime());
            //天数大于366天或者小于一天一定属于不合理输入，返回false
            if (time > 31622400000L || time < 0L) {
//                System.out.println("查询时间期间不合法。");
                return false;
            }

            //对字符串截取，取出年份和月份
            Integer beginYear = Integer.valueOf(beginDate.substring(0, 4));
            Integer beginMonth = Integer.valueOf(beginDate.substring(5, 7));
            Integer dueYear = Integer.valueOf(dueDate.substring(0, 4));
            Integer dueMonth = Integer.valueOf(dueDate.substring(5, 7));

            //判断是否为闰年，并跨过2月，如果是则增加一天的毫秒数
            if (isLeapYear(beginYear) && beginMonth <= 2) {
                ms += 86400000;
            } else if (isLeapYear(dueYear) && dueMonth >= 2) {
                ms += 86400000;
            }

            return time <= ms;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 给定一个年份，判断是否为闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(Integer year) {
        if (year % 100 == 0) {
            if (year % 400 == 0) {
                return true;
            }
        } else if (year % 4 == 0) {
            return true;
        }
        return false;
    }

    /**
     *
     */
    public static String LocalDateToString(LocalDate date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_PATTERN_YYYY_MM_DD);

        String dateStr = date.format(df);
       return dateStr;
    }

    /**
     * 判断时间是否大于1年
     */
    public static Boolean exceedDate(LocalDate date) {
        //获取一年后
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        Date time = cal.getTime();
        String dateStr = getDateStr(time, DATE_PATTERN_YYYY_MM_DD);
        String dateStr1 = getDateStr(Date.from(date.atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant()), DATE_PATTERN_YYYY_MM_DD);
        return checkIsOneYear(dateStr1, dateStr);
    }

    public static void main(String[] args) {
        Boolean aBoolean = exceedDate(LocalDate.now());
        System.out.println(aBoolean);
    }

    /**
     * String转LocalDate
     * @param date
     * @return
     */
    public static LocalDate StringToLocalDate(String date){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateParam = LocalDate.parse(date, df);
        return dateParam;
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDateBoolean(LocalDate nowTime, LocalDate startTime, LocalDate endTime) {
        if (nowTime.isEqual(startTime)
                || nowTime.isEqual(endTime) ) {
            return true;
        }


        if (nowTime.isAfter(startTime) && nowTime.isBefore(endTime)) {
            return true;
        } else {
            return false;
        }
    }
}
