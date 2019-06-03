package com.nablanet.aula31.repo.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Course extends KeyImpl {

    private CourseProfile profile;
    private Map<String, Member> members;

    public CourseProfile getProfile() {
        return profile;
    }

    public void setProfile(CourseProfile profile) {
        this.profile = profile;
    }

    public Map<String, Member> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Member> members) {
        this.members = members;
    }

    @Exclude
    final Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (profile != null) map.put("profile", profile.toMap());
        if (members != null) map.put("members", Member.toMap(members));
        return map;
    }

}
