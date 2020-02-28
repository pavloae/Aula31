package com.nablanet.aula31.classes;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void mergeLists() {
    }

    @Test
    public void isSameDay() {

        Assert.assertFalse(Util.isSameDay(null, null));
        Assert.assertFalse(Util.isSameDay(null, 1000L));
        Assert.assertFalse(Util.isSameDay(1000L, null));

        Assert.assertTrue(Util.isSameDay(0L,0L));

        Assert.assertTrue(Util.isSameDay(24 * 60 * 60 * 1000L, 23 * 60 * 60 * 1000L));

        Assert.assertFalse(Util.isSameDay(24 * 60 * 60 * 1000L, 48 * 60 * 60 * 1000L));

    }
}