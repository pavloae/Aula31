package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para parametrizar los valores en "/courses/$course_id/profile/subject/"
 * Forma parte de {@link CourseProfile}
 */
public class Subject extends KeyImpl {

    @Nullable private String id;
    @Nullable private String name;
    @Nullable private Integer grade;

    @Nullable
    public String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public Integer getGrade() {
        return grade;
    }

    public void setGrade(@Nullable Integer grade) {
        this.grade = grade;
    }

    @NonNull
    @Exclude
    final Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (id != null) map.put("id", id);
        if (name != null) map.put("fullName", name);
        if (grade != null) map.put("grade", grade);
        return map;
    }

}
