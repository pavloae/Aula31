package com.nablanet.aula31.utils;

import com.nablanet.aula31.repo.entity.ClassDay;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SortTest {

    @Test
    public void byDate() {

        List<ClassDay> classDayList = null;

        Sort.byDate(classDayList);
        Assert.assertNull(classDayList);

        classDayList = new ArrayList<>();
        Sort.byDate(classDayList);
        Assert.assertNotNull(classDayList);

        classDayList.add(new ClassDay("curso1", 1000L, "clase1", null));
        classDayList.add(new ClassDay("curso1", 2000L, "clase2", null));
        classDayList.add(new ClassDay("curso1", 3000L, "clase3", null));
        classDayList.add(new ClassDay("curso1", 4000L, "clase4", null));
        classDayList.add(new ClassDay("curso1", 5000L, "clase5", null));
        classDayList.add(new ClassDay("curso1", 6000L, "clase6", null));
        classDayList.add(new ClassDay("curso1", 7000L, "clase7", null));
        classDayList.add(new ClassDay("curso1", 8000L, "clase8", null));

        Assert.assertEquals("clase1", classDayList.get(0).comment);
        Assert.assertEquals("clase8", classDayList.get(7).comment);

        Sort.byDate(classDayList, Sort.DESC);
        Assert.assertEquals("clase8", classDayList.get(0).comment);
        Assert.assertEquals("clase1", classDayList.get(7).comment);

        Sort.byDate(classDayList);
        Assert.assertEquals("clase1", classDayList.get(0).comment);
        Assert.assertEquals("clase8", classDayList.get(7).comment);

    }
}