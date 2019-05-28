package com.nablanet.aula31.repo.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Course {

    @Exclude public String course_id;

    public CourseProfile profile;
    public Map<String, Member> members;

    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("profile", profile.toMap());
        if (members != null) map.put("members", Member.toMap(members));
        return map;
    }

}
