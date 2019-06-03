package com.nablanet.aula31.repo.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Membership extends KeyImpl {

    /**
     * State of user request to join course {@link #course_id}
     * Use with {@link #state}
     */
    public static final int REJECTED = 0;
    public static final int PENDING = 1;
    public static final int ACCEPTED = 2;
    public static final int EXPULSED = 3;

    /**
     * Role of memberItem on course {@link #course_id}
     * Use with {@link #role}
     */
    public static final int STUDENT = 0;
    public static final int TEACHER = 1;

    @Exclude
    public String membership_id;

    public String user_id;
    public String course_id;
    public String course_name;
    public String institution_name;
    public Integer role;
    public Integer state;

    public Membership() {
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("courseId", course_id);
        map.put("course_name", course_name);
        map.put("institution_name", institution_name);
        map.put("role", role);
        map.put("state", state);
        return map;
    }

}
