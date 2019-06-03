package com.nablanet.aula31.repo.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Subject extends KeyImpl {

    @Exclude private String key;

    private String id;
    private String name;
    private Integer grade;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Exclude
    final Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("fullName", name);
        map.put("grade", grade);
        return map;
    }

    @Exclude
    @Override
    public String getKey() {
        return key;
    }

    @Exclude
    @Override
    public void setKey(String key) {
        this.key = key;
    }

}
