package com.nablanet.aula31.repo.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ClassDay extends KeyImpl {

    public String course_id;
    public Long date;
    public String comment;
    public Map<String, Attendance> members;

    public ClassDay(){
    }

    public ClassDay(String course_id, Long date, String comment, Map<String, Attendance> members) {
        this.course_id = course_id;
        this.date = date;
        this.comment = comment;
        this.members = members;
    }

    @Exclude
    @NonNull
    public Long getDate() {
        if (date == null)
            date = 0L;
        return date;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("courseId", course_id);
        map.put("date", date);
        map.put("comment", comment);
        if (members != null) map.put("members", Attendance.toMap(members));
        return map;
    }

}
