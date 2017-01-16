package com.cbinfo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class MyDateUtil {
    // TODO: 28.12.2016 breadcrumbs (path)?
    // TODO: 30.12.2016 add Optional(last)

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public static Date toDate(String stringDate) throws ParseException {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date date = format.parse(stringDate);
        return date;
    }

    public static String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(date);
    }
}
