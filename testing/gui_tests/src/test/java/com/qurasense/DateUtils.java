package com.qurasense;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static String formatDate(Date date) {
        return ((SimpleDateFormat)DATE_FORMAT.clone()).format(date);
    }

    public static String formatDate(LocalDate localDate) {
        return DATE_FORMATTER.format(localDate);
    }

}
