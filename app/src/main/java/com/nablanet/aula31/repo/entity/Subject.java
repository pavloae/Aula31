package com.nablanet.aula31.repo.entity;

import java.util.HashMap;
import java.util.Map;

public class Subject {

    public String id;
    public String name;
    public Integer grade;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("fullName", name);
        map.put("grade", grade);
        return map;
    }

}
