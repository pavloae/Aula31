package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/classes/$class_id/members/$member_id/"
 * forma parte de {@link ClassDay}
 */
public class Attendance extends KeyImpl {

    @Nullable private Boolean present;
    @Nullable private Integer position;

    @Nullable
    public Boolean getPresent() {
        return present;
    }

    public void setPresent(@Nullable Boolean present) {
        this.present = present;
    }

    @Nullable
    public Integer getPosition() {
        return position;
    }

    public void setPosition(@Nullable Integer position) {
        this.position = position;
    }

    @NonNull
    @Exclude
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (present != null) map.put("present", present);
        if (position != null) map.put("position", position);
        return map;
    }

    @NonNull
    @Exclude
    public static Map<String, Object> toMap(@NonNull Map<String, Attendance> attendanceMap) {
        Map<String, Object> map = new HashMap<>();
        for (String key : attendanceMap.keySet()){
            Attendance attendance = attendanceMap.get(key);
            if (attendance != null)
                map.put(key, attendance.toMap());
        }
        return map;
    }

}
