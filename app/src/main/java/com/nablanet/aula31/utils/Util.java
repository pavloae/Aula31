package com.nablanet.aula31.utils;

import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    public static boolean isSameDay(long date1, long date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTimeInMillis(date1);
        cal2.setTimeInMillis(date2);

        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    public static boolean isBetween(@Nullable Long from, @Nullable Long to, @Nullable Long date) {
        if (date == null)
            return false;
        if (from == null && to == null)
            return true;
        if (from == null)
            return date <= to;
        if (to == null)
            return date >= from;
        return date >= from && date <= to;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }


}
