package com.nablanet.aula31.repo.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Member {

    @Exclude public static final int INACTIVE = 0;
    @Exclude public static final int PENDING = 1;
    @Exclude public static final int ACTIVE = 2;

    @Exclude public String member_id;

    private String user_id;
    private Profile profile;
    private Integer role;
    private Integer state;

    public Member() {
    }

    public String getUser_id() {
        return user_id;
    }

    public Profile getProfile() {
        return profile;
    }

    public Integer getRole() {
        return role;
    }

    public Integer getState() {
        return state;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    // Methodos obsoletos

    public void setUrl_image(String url_image) {
        if (profile == null)
            profile = new Profile();
        profile.url_image = url_image;
    }

    public void setLastname(String lastname) {
        if (profile == null)
            profile = new Profile();
        profile.lastname = lastname;
    }

    public void setNames(String names) {
        if (profile == null)
            profile = new Profile();
        profile.names = names;
    }

    public String getUrl_image() {
        return profile.url_image;
    }

    public String getLastname() {
        return profile.lastname;
    }

    public String getNames() {
        return profile.names;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("profile", profile.toMap());
        map.put("role", role);
        map.put("state", state);
        return map;
    }

    @Exclude
    public static  Map<String, Object> toMap(@NonNull Map<String, Member> memberMap) {
        Map<String, Object> map = new HashMap<>();
        for (String key : memberMap.keySet()){
            Member member = memberMap.get(key);
            if (member != null)
                map.put(key, member.toMap());
        }
        return map;
    }

}
