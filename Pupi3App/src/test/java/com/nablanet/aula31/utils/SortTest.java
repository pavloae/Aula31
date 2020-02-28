package com.nablanet.aula31.utils;

import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.repo.entity.Member;

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

        Assert.assertEquals("clase1", classDayList.get(0).getComment());
        Assert.assertEquals("clase8", classDayList.get(7).getComment());

        Sort.byDate(classDayList, Sort.DESC);
        Assert.assertEquals("clase8", classDayList.get(0).getComment());
        Assert.assertEquals("clase1", classDayList.get(7).getComment());

        Sort.byDate(classDayList);
        Assert.assertEquals("clase1", classDayList.get(0).getComment());
        Assert.assertEquals("clase8", classDayList.get(7).getComment());

    }

    @Test
    public void byFullName() {

        List<Member> memberList = new ArrayList<>();

        Member member;
        member = new Member(); member.setLastname("Zabala"); member.setNames("Marcela Roxana");
        memberList.add(member);

        member = new Member(); member.setLastname("Andino"); member.setNames("Esteban");
        memberList.add(member);

        member = new Member(); member.setLastname(null); member.setNames("Null");
        memberList.add(member);

        member = new Member(); member.setLastname("Andino"); member.setNames("Eduardo");
        memberList.add(member);

        member = new Member(); member.setLastname("Null"); member.setNames(null);
        memberList.add(member);

        member = new Member(); member.setLastname("García"); member.setNames("Juan");
        memberList.add(member);

        member = new Member(); member.setLastname(null); member.setNames(null);
        memberList.add(member);

        member = new Member(); member.setLastname("Marinelli"); member.setNames("Maninés");
        memberList.add(member);

        Sort.byFullName(memberList);

        Assert.assertNull(memberList.get(0).getLastname());
        Assert.assertNull(memberList.get(0).getNames());

        Assert.assertEquals("Null", memberList.get(1).getNames());

        Assert.assertEquals("Eduardo", memberList.get(2).getNames());

        Assert.assertEquals("Esteban", memberList.get(3).getNames());

        Assert.assertNull(memberList.get(6).getNames());

        Assert.assertEquals("Zabala", memberList.get(7).getLastname());



    }
}