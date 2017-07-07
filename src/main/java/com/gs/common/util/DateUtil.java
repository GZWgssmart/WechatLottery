package com.gs.common.util;

import com.gs.common.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Wang Genshen on 2017-07-06.
 */
public class DateUtil {

    public static Calendar stringToCalendar(String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_PATTERN);
        try {
            Date beginTimeDate = sdf.parse(timeStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(beginTimeDate);
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
