package ru.tcns.vktrgt.domain.util;

import org.joda.time.DateTime;
import org.joda.time.Years;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public static Integer getAge (String bday, String[] possibleFormats, Integer defaultValue) {
        try {
            Date b = org.apache.commons.lang.time.DateUtils.parseDate(bday, possibleFormats);
            DateTime bd = new DateTime(b);
            return Years.yearsBetween(bd, DateTime.now()).getYears();
        } catch (ParseException e) {
            return defaultValue;
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
