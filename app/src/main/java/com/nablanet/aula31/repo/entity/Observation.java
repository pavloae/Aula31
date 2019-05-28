package com.nablanet.aula31.repo.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Observation {

    @Exclude
    String observer_uid;

    public Integer rate;
    public String comment;

    public Observation(){
    }

    public Observation(Integer rate, String comment) {
        this.rate = rate;
        this.comment = comment;
    }

    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("rate", rate);
        map.put("comment", comment);
        return map;
    }

    public static  Map<String, Object> toMap(@NonNull Map<String, Observation> observationMap) {
        Map<String, Object> map = new HashMap<>();
        for (String key : observationMap.keySet()){
            Observation observation = observationMap.get(key);
            if (observation != null)
                map.put(key, observation.toMap());
        }
        return map;
    }

}
