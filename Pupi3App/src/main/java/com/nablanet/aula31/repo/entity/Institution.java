package com.nablanet.aula31.repo.entity;

import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/courses/$course_id/profile/institution"
 * forma parte de la clase {@link CourseProfile}
 */
public class Institution extends KeyImpl {

    @Nullable private String id;
    @Nullable private String name;

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

    @Exclude
    final Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (id != null) map.put("id", id);
        if (name != null) map.put("fullName", name);
        return map;
    }

}
