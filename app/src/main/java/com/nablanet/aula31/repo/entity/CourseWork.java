package com.nablanet.aula31.repo.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CourseWork extends KeyImpl {

    @Exclude public static final int HOMEWORK = 0;
    @Exclude public static final int CLASSWORK = 1;

    public String course_id;
    public Integer type;
    public Integer number;
    public String modality;
    public String name;
    public Long date;
    public String topics;
    public String resources_url;
    public Criteria criteria1;
    public Criteria criteria2;
    public Criteria criteria3;

    public CourseWork(){
    }

    @Exclude
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("courseId", course_id);
        map.put("type", type);
        map.put("modality", modality);
        map.put("fullName", name);
        map.put("date", date);
        map.put("topics", topics);
        map.put("resources_url", resources_url);
        if (criteria1 != null) map.put("criteria1", criteria1.toMap());
        if (criteria2 != null) map.put("criteria2", criteria2.toMap());
        if (criteria3 != null) map.put("criteria3", criteria3.toMap());
        return map;
    }

    public static class Criteria {
        public String name;
        public Integer weight;

        @Exclude
        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("fullName", name);
            map.put("weight", weight);
            return map;
        }
    }

}
