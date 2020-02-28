package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/courses/$course_id"
 */
public class Course extends KeyImpl {

    @Nullable private CourseProfile profile;
    @Nullable private Map<String, Member> members;

    @Nullable
    public CourseProfile getProfile() {
        return profile;
    }

    public void setProfile(@Nullable CourseProfile profile) {
        this.profile = profile;
    }

    @Nullable
    public Map<String, Member> getMembers() {
        return members;
    }

    public void setMembers(@Nullable Map<String, Member> members) {
        this.members = members;
    }

    @NonNull
    @Exclude
    final Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (profile != null) map.put("profile", profile.toMap());
        if (members != null) map.put("members", Member.toMap(members));
        return map;
    }

}
