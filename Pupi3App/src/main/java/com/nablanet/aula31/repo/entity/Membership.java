package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/memberships/$member_id/"
 */
public class Membership extends KeyImpl {

    /**
     * State of user request to join course {@link #course_id}
     * Use with {@link #state}
     */
    @Exclude public static final int REJECTED = 0;
    @Exclude public static final int PENDING = 1;
    @Exclude public static final int ACCEPTED = 2;
    @Exclude public static final int EXPULSED = 3;

    /**
     * Role of memberItem on course {@link #course_id}
     * Use with {@link #role}
     */
    @Exclude public static final int STUDENT = 0;
    @Exclude public static final int TEACHER = 1;

    @Nullable private String user_id;
    @Nullable private String course_id;
    @Nullable private String course_name;
    @Nullable private String institution_name;
    @Nullable private Integer role;
    @Nullable private Integer state;

    @Nullable
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(@Nullable String user_id) {
        this.user_id = user_id;
    }

    @Nullable
    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(@Nullable String course_id) {
        this.course_id = course_id;
    }

    ///// Legacy

    public String getCourseId() {
        return getCourse_id();
    }

    public void setCourseId(String courseId) {
        setCourse_id(courseId);
    }

    ////

    @Nullable
    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(@Nullable String course_name) {
        this.course_name = course_name;
    }

    @Nullable
    public String getInstitution_name() {
        return institution_name;
    }

    public void setInstitution_name(@Nullable String institution_name) {
        this.institution_name = institution_name;
    }

    @Nullable
    public Integer getRole() {
        return role;
    }

    public void setRole(@Nullable Integer role) {
        this.role = role;
    }

    @Nullable
    public Integer getState() {
        return state;
    }

    public void setState(@Nullable Integer state) {
        this.state = state;
    }

    @NonNull
    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (user_id != null) map.put("user_id", user_id);
        if (course_id != null) map.put("courseId", course_id);
        if (course_name != null) map.put("course_name", course_name);
        if (institution_name != null) map.put("institution_name", institution_name);
        if (role != null) map.put("role", role);
        if (state != null) map.put("state", state);
        return map;
    }

}
