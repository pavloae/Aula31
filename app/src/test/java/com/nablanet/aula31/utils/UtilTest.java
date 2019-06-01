package com.nablanet.aula31.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UtilTest {

    @Test
    public void getTimeFromMinutes() {

        Assert.assertEquals("8:00", Util.getTimeFromMinutes(8 * 60));
        Assert.assertEquals("8:30", Util.getTimeFromMinutes(8 * 60 + 30));

    }

    @Test
    public void getStartMarkMinute() {

        Assert.assertEquals(60, Util.getStartMarkMinute(48, 15));
        Assert.assertEquals(60, Util.getStartMarkMinute(59, 15));
        Assert.assertEquals(60, Util.getStartMarkMinute(60, 15));
        Assert.assertEquals(75, Util.getStartMarkMinute(61, 15));
        Assert.assertEquals(60, Util.getStartMarkMinute(48, 30));
        Assert.assertEquals(80, Util.getStartMarkMinute(61, 20));

        Assert.assertEquals(2019, Calendar.getInstance().get(Calendar.YEAR));

    }

    @Test
    public void sort() {

        List<ClassDay> classDayList = new ArrayList<>();

        classDayList.add(new ClassDay(5L, "Clase 5"));
        classDayList.add(new ClassDay(0L, "Clase 0"));
        classDayList.add(new ClassDay(8L, "Clase 8"));
        classDayList.add(new ClassDay(-3L, "Clase -3"));
        classDayList.add(new ClassDay(12L, "Clase 12"));
        classDayList.add(new ClassDay(4L, "Clase 4"));

        for (ClassDay classDay : classDayList)
            System.out.println(classDay.comment);

        System.out.println("===========================");

        Collections.sort(classDayList, Collections.reverseOrder(new Comparator<ClassDay>() {
            @Override
            public int compare(ClassDay o1, ClassDay o2) {
                return o2.date.compareTo(o1.date);
            }
        }));


        for (ClassDay classDay : classDayList)
            System.out.println(classDay.comment);


    }

    @Test
    public void getStringDate() {
    }

    @Test
    public void isSameDay() {
    }

    @Test
    public void isBetween() {

        Assert.assertTrue(Util.isBetween(0L, 1000L, null));
        Assert.assertTrue(Util.isBetween(null, 1000L, 2000L));

        Assert.assertFalse(Util.isBetween(2000L, 1000L, 1500L));
        Assert.assertFalse(Util.isBetween(null, 4000L, 2000L));


    }

    @Test
    public void isExternalStorageWritable() {
    }

    @Test
    public void isExternalStorageReadable() {
    }

    public class ClassDay {
        public Long date;
        public String comment;

        public ClassDay(Long date, String comment) {
            this.date = date;
            this.comment = comment;
        }
    }

}