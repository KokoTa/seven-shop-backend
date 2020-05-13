package com.example.shop.util;

import com.example.shop.bo.PageCounter;

import java.math.BigDecimal;
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

    /**
     * 判断订单是否过期
     * @param placeTime 订单创建时间
     * @param period 配置文件的过期时间间隔
     * @return
     */
    public static Boolean isOutOfDate(Date placeTime, Long period) {
        Long now = Calendar.getInstance().getTimeInMillis();
        Long placeTimeStamp = placeTime.getTime();
        Long periodMillSecond = period * 1000;
        return now > (placeTimeStamp + periodMillSecond);
    }

    /**
     * 判断订单是否过期
     * @param expiredTime 订单过期时间
     * @return
     */
    public static Boolean isOutOfDate(Date expiredTime) {
        Long now = Calendar.getInstance().getTimeInMillis();
        Long expiredTimeStamp = expiredTime.getTime();
        return now > expiredTimeStamp;
    }

    /**
     * 元转分
     * @param b
     * @return
     */
    public static Integer yuanToFenPlainString(BigDecimal b) {
        b = b.multiply(new BigDecimal("100"));
        // stripTrailingZeros 去除小数点后多余的0
        // toString 在数值大的时候会变成科学计数法，使用 toPlainString 可以正确输出
        // return b.stripTrailingZeros().toPlainString();
        return b.intValue();
    }
}
