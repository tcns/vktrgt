package ru.tcns.vktrgt.domain.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TIMUR on 16.04.2016.
 */
public class DateUtils {
    private DateUtils(){}

    public static Date parseString(String string, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(string);
        } catch (ParseException e) {
            return null;
        }
    }
    public static Date parseUnixTime(String string) {
        Long unixTime = 0L;
        try {
            unixTime = Long.valueOf(string);
        } catch (NumberFormatException e) {
            return null;
        }
        return new Date(unixTime);
    }
}
