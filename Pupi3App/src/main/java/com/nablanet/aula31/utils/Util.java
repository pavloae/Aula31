package com.nablanet.aula31.utils;

import androidx.annotation.Nullable;

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

    @Nullable
    public static String getStringDate(Long date) {
        if (date == null) return null;
        return new SimpleDateFormat(
                "E dd 'de' MMMM",
                Locale.getDefault()
        ).format(new Date(date));
    }

    public static int getStartMarkMinute(int startMinute, int intervaleSizeMinute) {
        return (startMinute % intervaleSizeMinute == 0) ? startMinute :
                startMinute + intervaleSizeMinute - ( startMinute % intervaleSizeMinute );
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

}
