package com.nablanet.aula31.repo.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Course {

    @Exclude public String courseId;

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

    final Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("profile", profile.toMap());
        if (members != null) map.put("members", Member.toMap(members));
        return map;
    }

}
