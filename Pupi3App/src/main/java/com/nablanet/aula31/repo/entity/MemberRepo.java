package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/works/$member_id/"
 */
public class MemberRepo extends KeyImpl {

    @Nullable private String user_id;
    @Nullable private String course_id;
    @Nullable private Profile profile;
    @Nullable private Map<String, MemberWork> works;

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

    @Nullable
    public Profile getProfile() {
        return profile;
    }

    public void setProfile(@Nullable Profile profile) {
        this.profile = profile;
    }

    @Nullable
    public Map<String, MemberWork> getWorks() {
        return works;
    }

    public void setWorks(@Nullable Map<String, MemberWork> works) {
        this.works = works;
    }

    @Exclude
    public String getFullName() {
        if (profile == null) return ",";
        return String.format(
                Locale.getDefault(),
                "%s, %s",
                (profile.getLastname() == null) ? "" : profile.getLastname(),
                (profile.getNames() == null) ? "" : profile.getNames()
        );
    }

    @NonNull
    @Exclude
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (user_id != null) map.put("user_id", user_id);
        if (course_id != null) map.put("courseId", course_id);
        if (profile != null) map.put("profile", profile.toMap());
        if (works != null) map.put("works", MemberWork.toMap(works));
        return map;
    }

}
