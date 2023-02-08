package com.mogudiandian.util.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * JDK8 LocalDate的工具类
 *
 * @author sunbo
 */
public final class LocalDateUtils {

    private LocalDateUtils() {}

    /**
     * Date -> LocalDate
     * @param date 老日期
     * @return 新日期
     */
    public static LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toLocalDate();
    }

    /**
     * Date -> LocalDateTime
     * @param date 老日期
     * @return 新日期
     */
    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toLocalDateTime();
    }

    /**
     * LocalDate -> Date
     * @param date 新日期
     * @return 老日期
     */
    public static Date convert(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime -> Date
     * @param date 新日期
     * @return 老日期
     */
    public static Date convert(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

}
