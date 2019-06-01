package com.nablanet.aula31.utils;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.nablanet.aula31.repo.entity.ClassDay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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

    /**
     * Analiza si la fecha ingresada se encuentra entre dos límites. Compara los milisegundo
     *
     * @param from Fecha límite inferior especificada por un valor Long indicando los milisegundos
     * @param to Fecha límite superior especificada por un valor Long indicando los milisegundos
     * @param date Fecha a contemplar
     * @return Verdadero si no hay límites o la fecha ingresada no es nula y
     * se encuentra entre los límites, si estos existen.
     */
    public static boolean isBetween(@Nullable Long from, @Nullable Long date, @Nullable Long to) {
        if (from == null && to == null)
            return true;
        if (from == null && date != null)
            return date <= to;
        if (to == null && date != null)
            return date >= from;
        if (date == null)
            return false;
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
