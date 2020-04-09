package com.example.shop.util;

import com.example.shop.bo.PageCounter;

import java.util.Calendar;
import java.util.Date;

public class CommonUtil {

    /**
     * 将 start、count 转为 pageNo、pageSize
     * @param start
     * @param count
     * @return
     */
    public static PageCounter convertToPageParameter(Integer start, Integer count) {
        Integer pageNo = start / count;
        return PageCounter.builder().pageNo(pageNo).pageSize(count).build();
    }

    /**
     * 是否在某个时间范围内
     * @param date
     * @param start
     * @param end
     * @return
     */
    public static Boolean isInTime(Date date, Date start, Date end) {
        long time = date.getTime();
        long startTime = start.getTime();
        long endTime = end.getTime();
        return time > startTime && time < endTime;
    }

    /**
     * 获取当前时间加上一段时间后的时间
     * @param seconds
     * @return
     */
    public static Calendar getExpiredTime(Integer seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        return calendar;
    }
}
