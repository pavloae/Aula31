package com.nablanet.aula31.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static String getTimeFromMinutes(int totalMinutes) {
        return String.format(
                Locale.getDefault(),
                "%d:%02d",
                totalMinutes / 60,
                totalMinutes % 60);
    }

    public static String getStringDate(long date) {
        return new SimpleDateFormat(
                "E dd 'de' MMMM",
                Locale.getDefault()
        ).format(new Date(date));
    }

    public static int getStartMarkMinute(int startMinute, int intervaleSizeMinute) {
        return (startMinute % intervaleSizeMinute == 0) ? startMinute :
                startMinute + intervaleSizeMinute - ( startMinute % intervaleSizeMinute );
    }


}
