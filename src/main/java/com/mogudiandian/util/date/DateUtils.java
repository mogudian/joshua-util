package com.mogudiandian.util.date;

import java.util.*;

/**
 * 日期工具类
 * @author Joshua Sun
 */
public final class DateUtils {

    private static final Map<DateParser, String> dateParsersMap = new LinkedHashMap<>();

    static {
        // region yMdHmsS
        dateParsersMap.put(new DateParser("yyyy-MM-dd HH:mm:ss.SSS"), "yMdHmsS");
        dateParsersMap.put(new DateParser("dd/MM/yyyy HH:mm:ss.SSS"), "yMdHmsS");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH时mm分ss秒SSS"), "yMdHmsS");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH点mm分ss秒SSS"), "yMdHmsS");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH时mm分ss秒SSS"), "yMdHmsS");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH点mm分ss秒SSS"), "yMdHmsS");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH时mm分ss秒SSS"), "yMdHmsS");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH点mm分ss秒SSS"), "yMdHmsS");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH时mm分ss秒SSS"), "yMdHmsS");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH点mm分ss秒SSS"), "yMdHmsS");
        // endregion

        // region yMdHms
        dateParsersMap.put(new DateParser("yyyy-MM-dd HH:mm:ss"), "yMdHms");
        dateParsersMap.put(new DateParser("dd/MM/yyyy HH:mm:ss"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH时mm分ss秒"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH点mm分ss秒"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH时mm分ss秒"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH点mm分ss秒"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH时mm分ss"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH点mm分ss"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH时mm分ss"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH点mm分ss"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH时mm分ss秒"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH点mm分ss秒"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH时mm分ss秒"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH点mm分ss秒"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH时mm分ss"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH点mm分ss"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH时mm分ss"), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH点mm分ss"), "yMdHms");

        dateParsersMap.put(new DateParser("yyyy-MM-dd AM hh:mm:ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy-MM-dd PM hh:mm:ss", false), "yMdHms");
        dateParsersMap.put(new DateParser("dd/MM/yyyy AM hh:mm:ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("dd/MM/yyyy PM hh:mm:ss", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日凌晨hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日凌晨hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早上hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早上hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早晨hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早晨hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日上午hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日上午hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日下午hh时mm分ss秒", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日下午hh点mm分ss秒", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日凌晨hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日凌晨hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早上hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早上hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早晨hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早晨hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日上午hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日上午hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日下午hh时mm分ss", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日下午hh点mm分ss", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 凌晨hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 凌晨hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早上晨hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早上hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早晨晨hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早晨hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 上午hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 上午hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 下午hh时mm分ss秒", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 下午hh点mm分ss秒", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 凌晨hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 凌晨hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早晨hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早晨hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 上午hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 上午hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 下午hh时mm分ss", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 下午hh点mm分ss", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号凌晨hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号凌晨hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早上hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早上hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早晨hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早晨hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号上午hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号上午hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号下午hh时mm分ss秒", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号下午hh点mm分ss秒", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号凌晨hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号凌晨hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早上hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早上hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早晨hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早晨hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号上午hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号上午hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号下午hh时mm分ss", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号下午hh点mm分ss", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 凌晨hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 凌晨hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早上hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早上hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早晨hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早晨hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 上午hh时mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 上午hh点mm分ss秒", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 下午hh时mm分ss秒", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 下午hh点mm分ss秒", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 凌晨hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 凌晨hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早上hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早上hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早晨hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早晨hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 上午hh时mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 上午hh点mm分ss", true), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 下午hh时mm分ss", false), "yMdHms");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 下午hh点mm分ss", false), "yMdHms");
        // endregion

        // region yMdHm
        dateParsersMap.put(new DateParser("yyyy-MM-dd HH:mm"), "yMdHm");
        dateParsersMap.put(new DateParser("dd/MM/yyyy HH:mm"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH时mm分"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH点mm分"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH时mm分"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH点mm分"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH时mm"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH点mm"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH时mm"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH点mm"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH时mm分"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH点mm分"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH时mm分"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH点mm分"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH时mm"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH点mm"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH时mm"), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH点mm"), "yMdHm");

        dateParsersMap.put(new DateParser("yyyy-MM-dd AM hh:mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy-MM-dd PM hh:mm", false), "yMdHm");
        dateParsersMap.put(new DateParser("dd/MM/yyyy AM hh:mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("dd/MM/yyyy PM hh:mm", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日凌晨hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日凌晨hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早上hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早上hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早晨hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早晨hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日上午hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日上午hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日下午hh时mm分", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日下午hh点mm分", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日凌晨hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日凌晨hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早上hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早上hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早晨hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早晨hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日上午hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日上午hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日下午hh时mm", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日下午hh点mm", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 凌晨hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 凌晨hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早上hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早上hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早晨hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早晨hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 上午hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 上午hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 下午hh时mm分", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 下午hh点mm分", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 凌晨hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 凌晨hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早上hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早上hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早晨hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早晨hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 上午hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 上午hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 下午hh时mm", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 下午hh点mm", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号凌晨hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号凌晨hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早上hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早上hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早晨hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早晨hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号上午hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号上午hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号下午hh时mm分", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号下午hh点mm分", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号凌晨hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号凌晨hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早上hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早上hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早晨hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早晨hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号上午hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号上午hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号下午hh时mm", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号下午hh点mm", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 凌晨hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 凌晨hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早上hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早上hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早晨hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早晨hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 上午hh时mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 上午hh点mm分", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 下午hh时mm分", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 下午hh点mm分", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 凌晨hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 凌晨hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早上hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早上hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早晨hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早晨hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 上午hh时mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 上午hh点mm", true), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 下午hh时mm", false), "yMdHm");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 下午hh点mm", false), "yMdHm");
        // endregion

        // region yMdH
        dateParsersMap.put(new DateParser("yyyy-MM-dd HH o'clock"), "yMdH");
        dateParsersMap.put(new DateParser("dd/MM/yyyy HH o'clock"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH时"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH点"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH时"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH点"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH时"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日HH点"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH时"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 HH点"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH时"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH点"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH时"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH点"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH时"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号HH点"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH时"), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 HH点"), "yMdH");

        dateParsersMap.put(new DateParser("yyyy-MM-dd AM hh o'clock", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy-MM-dd PM hh o'clock", false), "yMdH");
        dateParsersMap.put(new DateParser("dd/MM/yyyy AM hh o'clock", true), "yMdH");
        dateParsersMap.put(new DateParser("dd/MM/yyyy PM hh o'clock", false), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日凌晨hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日凌晨hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早上hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早上hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早晨hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早晨hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日早hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日上午hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日上午hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日下午hh时", false), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日下午hh点", false), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 凌晨hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 凌晨hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早上hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早上hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早晨hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早晨hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 早hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 上午hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 上午hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 下午hh时", false), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日 下午hh点", false), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号凌晨hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号凌晨hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早上hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早上hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早晨hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早晨hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号早hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号上午hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号上午hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号下午hh时", false), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号下午hh点", false), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 凌晨hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 凌晨hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早上hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早上hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早晨hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早晨hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 早hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 上午hh时", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 上午hh点", true), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 下午hh时", false), "yMdH");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号 下午hh点", false), "yMdH");
        // endregion

        // region yMd
        dateParsersMap.put(new DateParser("yyyyMMdd"), "yMd");
        dateParsersMap.put(new DateParser("yyyy-MM-dd"), "yMd");
        dateParsersMap.put(new DateParser("yyyy.MM.dd"), "yMd");
        dateParsersMap.put(new DateParser("MM.dd,yyyy"), "yMd");
        dateParsersMap.put(new DateParser("MM.dd, yyyy"), "yMd");
        dateParsersMap.put(new DateParser("dd/MM/yyyy"), "yMd");
        dateParsersMap.put(new DateParser("yyyy年MM月dd日"), "yMd");
        dateParsersMap.put(new DateParser("yyyy年MM月dd号"), "yMd");
        dateParsersMap.put(new DateParser("yyyy年MM月dd"), "yMd");
        dateParsersMap.put(new DateParser("yyyy·MM·dd"), "yMd");
        dateParsersMap.put(new DateParser("yyyy年·MM月·dd日"), "yMd");
        dateParsersMap.put(new DateParser("yyyy年·MM月·dd号"), "yMd");
        // endregion

        // region yM 注释的和MMdd冲突了
        /*dateParsersMap.put(new DateParser("yyyyMM"), "yM");
        dateParsersMap.put(new DateParser("yyyy-MM"), "yM");
        dateParsersMap.put(new DateParser("yyyy.MM"), "yM");
        dateParsersMap.put(new DateParser("MM/yyyy"), "yM");*/
        dateParsersMap.put(new DateParser("yyyy年MM月"), "yM");
        dateParsersMap.put(new DateParser("yyyy年MM"), "yM");
        dateParsersMap.put(new DateParser("yyyy·MM"), "yM");
        dateParsersMap.put(new DateParser("yyyy年·MM月"), "yM");
        // endregion

        // region MdHmsS
        dateParsersMap.put(new DateParser("MM-dd HH:mm:ss.SSS"), "MdHmsS");
        dateParsersMap.put(new DateParser("dd/MM HH:mm:ss.SSS"), "MdHmsS");
        dateParsersMap.put(new DateParser("MM月dd日HH时mm分ss秒SSS"), "MdHmsS");
        dateParsersMap.put(new DateParser("MM月dd日HH点mm分ss秒SSS"), "MdHmsS");
        dateParsersMap.put(new DateParser("MM月dd日 HH时mm分ss秒SSS"), "MdHmsS");
        dateParsersMap.put(new DateParser("MM月dd日 HH点mm分ss秒SSS"), "MdHmsS");
        dateParsersMap.put(new DateParser("MM月dd号HH时mm分ss秒SSS"), "MdHmsS");
        dateParsersMap.put(new DateParser("MM月dd号HH点mm分ss秒SSS"), "MdHmsS");
        dateParsersMap.put(new DateParser("MM月dd号 HH时mm分ss秒SSS"), "MdHmsS");
        dateParsersMap.put(new DateParser("MM月dd号 HH点mm分ss秒SSS"), "MdHmsS");
        // endregion

        // region MdHms
        dateParsersMap.put(new DateParser("MM-dd HH:mm:ss"), "MdHms");
        dateParsersMap.put(new DateParser("dd/MM HH:mm:ss"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日HH时mm分ss秒"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日HH点mm分ss秒"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 HH时mm分ss秒"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 HH点mm分ss秒"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日HH时mm分ss"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日HH点mm分ss"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 HH时mm分ss"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 HH点mm分ss"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号HH时mm分ss秒"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号HH点mm分ss秒"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 HH时mm分ss秒"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 HH点mm分ss秒"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号HH时mm分ss"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号HH点mm分ss"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 HH时mm分ss"), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 HH点mm分ss"), "MdHms");

        dateParsersMap.put(new DateParser("MM-dd AM hh:mm:ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM-dd PM hh:mm:ss", false), "MdHms");
        dateParsersMap.put(new DateParser("dd/MM AM hh:mm:ss", true), "MdHms");
        dateParsersMap.put(new DateParser("dd/MM PM hh:mm:ss", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日凌晨hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日凌晨hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日早上hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日早上hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日早晨hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日早晨hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日早hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日早hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日上午hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日上午hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日下午hh时mm分ss秒", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日下午hh点mm分ss秒", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日凌晨hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日凌晨hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日早上hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日早上hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日早晨hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日早晨hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日早hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日早hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日上午hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日上午hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日下午hh时mm分ss", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日下午hh点mm分ss", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 凌晨hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 凌晨hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 早上hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 早上hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 早晨hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 早晨hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 早hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 早hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 上午hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 上午hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 下午hh时mm分ss秒", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 下午hh点mm分ss秒", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 凌晨hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 凌晨hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 早上hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 早上hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 早晨hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 早晨hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 早hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 早hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 上午hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 上午hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 下午hh时mm分ss", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd日 下午hh点mm分ss", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号凌晨hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号凌晨hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号早上hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号早上hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号早晨hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号早晨hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号早hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号早hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号上午hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号上午hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号下午hh时mm分ss秒", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号下午hh点mm分ss秒", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号凌晨hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号凌晨hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号早上hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号早上hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号早晨hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号早晨hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号早hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号早hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号上午hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号上午hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号下午hh时mm分ss", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号下午hh点mm分ss", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 凌晨hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 凌晨hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 早上hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 早上hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 早晨hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 早晨hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 早hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 早hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 上午hh时mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 上午hh点mm分ss秒", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 下午hh时mm分ss秒", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 下午hh点mm分ss秒", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 凌晨hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 凌晨hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 早上hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 早上hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 早晨hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 早晨hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 早hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 早hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 上午hh时mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 上午hh点mm分ss", true), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 下午hh时mm分ss", false), "MdHms");
        dateParsersMap.put(new DateParser("MM月dd号 下午hh点mm分ss", false), "MdHms");
        // endregion

        // region MdHm
        dateParsersMap.put(new DateParser("MM-dd HH:mm"), "MdHm");
        dateParsersMap.put(new DateParser("dd/MM HH:mm"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日HH时mm分"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日HH点mm分"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 HH时mm分"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 HH点mm分"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日HH时mm"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日HH点mm"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 HH时mm"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 HH点mm"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号HH时mm分"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号HH点mm分"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 HH时mm分"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 HH点mm分"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号HH时mm"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号HH点mm"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 HH时mm"), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 HH点mm"), "MdHm");

        dateParsersMap.put(new DateParser("MM-dd AM hh:mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM-dd PM hh:mm", false), "MdHm");
        dateParsersMap.put(new DateParser("dd/MM AM hh:mm", true), "MdHm");
        dateParsersMap.put(new DateParser("dd/MM PM hh:mm", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日凌晨hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日凌晨hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日早上hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日早上hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日早晨hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日早晨hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日早hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日早hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日上午hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日上午hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日下午hh时mm分", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日下午hh点mm分", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日凌晨hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日凌晨hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日早上hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日早上hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日早晨hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日早晨hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日早hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日早hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日上午hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日上午hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日下午hh时mm", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日下午hh点mm", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 凌晨hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 凌晨hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 早上hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 早上hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 早晨hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 早晨hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 早hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 早hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 上午hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 上午hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 下午hh时mm分", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 下午hh点mm分", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 凌晨hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 凌晨hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 早上hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 早上hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 早晨hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 早晨hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 早hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 早hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 上午hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 上午hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 下午hh时mm", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd日 下午hh点mm", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号凌晨hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号凌晨hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号早上hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号早上hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号早晨hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号早晨hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号早hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号早hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号上午hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号上午hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号下午hh时mm分", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号下午hh点mm分", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号凌晨hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号凌晨hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号早上hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号早上hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号早晨hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号早晨hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号早hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号早hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号上午hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号上午hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号下午hh时mm", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号下午hh点mm", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 凌晨hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 凌晨hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 早上hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 早上hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 早晨hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 早晨hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 早hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 早hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 上午hh时mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 上午hh点mm分", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 下午hh时mm分", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 下午hh点mm分", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 凌晨hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 凌晨hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 早上hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 早上hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 早晨hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 早晨hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 早hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 早hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 上午hh时mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 上午hh点mm", true), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 下午hh时mm", false), "MdHm");
        dateParsersMap.put(new DateParser("MM月dd号 下午hh点mm", false), "MdHm");
        // endregion

        // region MdH
        dateParsersMap.put(new DateParser("MM-dd HH o'clock"), "MdH");
        dateParsersMap.put(new DateParser("dd/MM HH o'clock"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日HH时"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日HH点"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 HH时"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 HH点"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日HH时"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日HH点"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 HH时"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 HH点"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号HH时"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号HH点"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 HH时"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 HH点"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号HH时"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号HH点"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 HH时"), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 HH点"), "MdH");

        dateParsersMap.put(new DateParser("MM-dd AM hh o'clock", true), "MdH");
        dateParsersMap.put(new DateParser("MM-dd PM hh o'clock", false), "MdH");
        dateParsersMap.put(new DateParser("dd/MM AM hh o'clock", true), "MdH");
        dateParsersMap.put(new DateParser("dd/MM PM hh o'clock", false), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日凌晨hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日凌晨hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日早上hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日早上hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日早晨hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日早晨hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日早hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日早hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日上午hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日上午hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日下午hh时", false), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日下午hh点", false), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 凌晨hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 凌晨hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 早上hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 早上hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 早晨hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 早晨hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 早hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 早hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 上午hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 上午hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 下午hh时", false), "MdH");
        dateParsersMap.put(new DateParser("MM月dd日 下午hh点", false), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号凌晨hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号凌晨hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号早上hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号早上hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号早晨hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号早晨hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号早hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号早hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号上午hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号上午hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号下午hh时", false), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号下午hh点", false), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 凌晨hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 凌晨hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 早上hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 早上hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 早晨hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 早晨hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 早hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 早hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 上午hh时", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 上午hh点", true), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 下午hh时", false), "MdH");
        dateParsersMap.put(new DateParser("MM月dd号 下午hh点", false), "MdH");
        // endregion

        // region Md
        dateParsersMap.put(new DateParser("MM-dd"), "Md");
        dateParsersMap.put(new DateParser("MM.dd"), "Md");
        dateParsersMap.put(new DateParser("dd/MM"), "Md");
        dateParsersMap.put(new DateParser("MM月dd日"), "Md");
        dateParsersMap.put(new DateParser("MM月dd号"), "Md");
        dateParsersMap.put(new DateParser("MM月dd"), "Md");
        dateParsersMap.put(new DateParser("MM·dd"), "Md");
        dateParsersMap.put(new DateParser("MM月·dd日"), "Md");
        dateParsersMap.put(new DateParser("MM月·dd号"), "Md");
        // endregion

        // region y
        dateParsersMap.put(new DateParser("yyyy"), "y");
        dateParsersMap.put(new DateParser("yyyy年"), "y");
        // endregion
    }

    private DateUtils() {
        super();
    }

    /**
     * 将[任意样式]字符串解析为时间
     * @param str 字符串
     * @return 时间
     */
    public static Date parse(String str) {
        // 遍历每一个解析器 如果能解析(不返回空)表示解析成功 则返回
        for (Map.Entry<DateParser, String> entry : dateParsersMap.entrySet()) {
            DateParser dateParser = entry.getKey();
            Date date = dateParser.parse(str);
            if (date != null) {
                return date;
            }
        }
        // 到这里表示无能为力了
        return null;
    }

    /**
     * 根据时间字符串推导所有串
     * @param str 字符串
     * @return 所有格式的字符串
     */
    public static List<String> derive(String str) {
        Date date = null;
        String code = null;
        for (Map.Entry<DateParser, String> entry : dateParsersMap.entrySet()) {
            DateParser dateParser = entry.getKey();
            date = dateParser.parse(str);
            if (date != null) {
                code = entry.getValue();
                break;
            }
        }
        if (date == null) {
            return Collections.emptyList();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        List<String> list = new ArrayList<>();
        for (Map.Entry<DateParser, String> entry : dateParsersMap.entrySet()) {
            String value = entry.getValue();
            if (code.contains(value)) {
                DateParser dateParser = entry.getKey();
                if (dateParser.getAmPm() != null) {
                    if (dateParser.getAmPm() && calendar.get(Calendar.AM_PM) == Calendar.PM
                            || !dateParser.getAmPm() && calendar.get(Calendar.AM_PM) == Calendar.AM) {
                        continue;
                    }
                }
                String formatWithZeroPadding = dateParser.format(date, true);
                list.add(formatWithZeroPadding);
                String format = dateParser.format(date, false);
                if (!format.equals(formatWithZeroPadding)) {
                    list.add(format);
                }
            }
        }
        return list;
    }
}
