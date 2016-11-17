package com.cbinfo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class myDateUtil {

    public static Date toDate(String stringDate) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd/mm/yyyy");
        Date date = format.parse(stringDate);
        return date;
    }
}
