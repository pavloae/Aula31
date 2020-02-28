package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/classes/$class_id/"
 */
public class ClassDay extends KeyImpl {

    @Nullable private String course_id;
    @Nullable private Long date;
    @Nullable private String comment;
    @Nullable private Map<String, Attendance> members;

    public ClassDay(){
    }

    public ClassDay(
            @Nullable String course_id, @Nullable Long date,
            @Nullable String comment, @Nullable Map<String, Attendance> members
    ) {
        this.course_id = course_id;
        this.date = date;
        this.comment = comment;
        this.members = members;
    }

    @Nullable
    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(@Nullable String course_id) {
        this.course_id = course_id;
    }

    public void setDate(@Nullable Long date) {
        this.date = date;
    }

    @Nullable
    public String getComment() {
        return comment;
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    @Nullable
    public Map<String, Attendance> getMembers() {
        return members;
    }

    public void setMembers(@Nullable Map<String, Attendance> members) {
        this.members = members;
    }

    @Nullable
    public Long getDate() {
        return date;
    }

    @NonNull
    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (course_id != null) map.put("courseId", course_id);
        if (date != null) map.put("date", date);
        if (comment != null) map.put("comment", comment);
        if (members != null) map.put("members", Attendance.toMap(members));
        return map;
    }

}
