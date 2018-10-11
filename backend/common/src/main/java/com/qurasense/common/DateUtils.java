package com.qurasense.common;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public final class DateUtils {

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private DateUtils() {
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String formatDateTime(Date date) {
        return ((SimpleDateFormat)DATE_TIME_FORMAT.clone()).format(date);
    }

}
