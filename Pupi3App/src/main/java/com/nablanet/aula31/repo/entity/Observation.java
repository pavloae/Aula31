package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en
 * "/tracking/$member_id/classes/$class_id/observations/$observer_uid/"
 * Forma parte de la clase {@link ClassTrack}
 */
public class Observation extends KeyImpl {

    @Nullable private Integer rate;
    @Nullable private String comment;

    public Observation(){
    }

    public Observation(@Nullable Integer rate, @Nullable String comment) {
        this.rate = rate;
        this.comment = comment;
    }

    @Nullable
    public Integer getRate() {
        return rate;
    }

    public void setRate(@Nullable Integer rate) {
        this.rate = rate;
    }

    @Nullable
    public String getComment() {
        return comment;
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    @NonNull
    @Exclude
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (rate != null) map.put("rate", rate);
        if (comment != null) map.put("comment", comment);
        return map;
    }

    @NonNull
    @Exclude
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
