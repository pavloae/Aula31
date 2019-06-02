package com.nablanet.aula31.repo.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MemberTrack {

    @Exclude public String member_id;

    public String user_id;
    public String course_id;
    public Profile profile;
    public Map<String, ClassTrack> classes;

    @Exclude
    public String getFullName() {
        if (profile == null) return ",";
        return String.format(
                Locale.getDefault(),
                "%s, %s",
                (profile.lastname == null) ? "" : profile.lastname,
                (profile.names == null) ? "" : profile.names
        );
    }

    @Exclude
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("courseId", course_id);
        map.put("profile", profile.toMap());
        if (classes != null) map.put("classes", ClassTrack.toMap(classes));
        return map;
    }

}
