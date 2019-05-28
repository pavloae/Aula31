package com.nablanet.aula31.repo;

import com.nablanet.aula31.repo.entity.Attendance;
import com.nablanet.aula31.repo.entity.ClassDay;

import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ClassDayExtTest {

    @Test
    public void toMap() {

        ClassDay classDay = new ClassDay();
        classDay.course_id = "curso1";
        classDay.date = 12512354856L;
        classDay.comment = "Clase de matematica";
        classDay.members = createMemberMap();

        System.out.println(new JSONObject(classDay.toMap()).toString());

        classDay = new ClassDay();
        classDay.course_id = "curso2";
        classDay.date = 1251212356L;
        classDay.comment = "Clase de lengua";

        System.out.println(new JSONObject(classDay.toMap()).toString());

    }

    @Test
    public void membersToMap() {

        System.out.println(new JSONObject(createMemberMap()).toString());

    }

    private Map<String, Attendance> createMemberMap() {

        Map<String, Attendance> memberMap = new HashMap<>();
        memberMap.put("miembro1", new Attendance(true, 5));
        memberMap.put("miembro2", new Attendance(false, null));
        memberMap.put("miembro3", new Attendance(true, null));
        memberMap.put("miembro4", new Attendance(false, 1));
        memberMap.put("miembro5", new Attendance(null, null));
        memberMap.put("miembro6", new Attendance(null, 4));
        memberMap.put("miembro1", new Attendance(false, 7));

        return memberMap;

    }

}