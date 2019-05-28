package com.nablanet.aula31.repo.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CourseProfile {

    @Exclude public String course_id;

    public Institution institution;
    public Subject subject;
    public String classroom;
    public Integer year;
    public String owner;
    public String shift;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("institution", institution.toMap());
        map.put("subject", subject.toMap());
        map.put("classroom", classroom);
        map.put("year", year);
        map.put("owner", owner);
        map.put("shift", shift);
        return map;
    }

}
