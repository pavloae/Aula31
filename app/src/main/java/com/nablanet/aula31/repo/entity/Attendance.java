package com.nablanet.aula31.repo.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Attendance {

    @Exclude
    public String member_id;

    private Boolean present;
    private Integer position;

    public Attendance() {
    }

    public Attendance(Boolean present, Integer position) {
        this.present = present;
        this.position = position;
    }

    public Boolean getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Exclude
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("present", present);
        map.put("position", position);
        return map;
    }

    @Exclude
    public static  Map<String, Object> toMap(@NonNull Map<String, Attendance> attendanceMap) {
        Map<String, Object> map = new HashMap<>();
        for (String key : attendanceMap.keySet()){
            Attendance attendance = attendanceMap.get(key);
            if (attendance != null)
                map.put(key, attendance.toMap());
        }
        return map;
    }

}
