package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Criteria {

    @Nullable private String name;
    @Nullable private Integer weight;

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(@Nullable Integer weight) {
        this.weight = weight;
    }

    @NonNull
    @Exclude
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (name != null) map.put("fullName", name);
        if (weight != null) map.put("weight", weight);
        return map;
    }
}
