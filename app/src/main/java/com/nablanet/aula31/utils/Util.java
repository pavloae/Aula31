package com.nablanet.aula31.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
