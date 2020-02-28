package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/courses/$course_id/members/$member_id/"
 */
public class Member extends KeyImpl {

    @Exclude public static final int INACTIVE = 0;
    @Exclude public static final int PENDING = 1;
    @Exclude public static final int ACTIVE = 2;

    @Nullable private String user_id;
    @Nullable private Profile profile;
    @Nullable private Integer role;
    @Nullable private Integer state;

    public void setUser_id(@Nullable String user_id) {
        this.user_id = user_id;
    }

    @Nullable
    public String getUser_id() {
        return user_id;
    }

    public void setProfile(@Nullable Profile profile) {
        this.profile = profile;
    }

    @Nullable
    public Profile getProfile() {
        return profile;
    }

    public void setRole(@Nullable Integer role) {
        this.role = role;
    }

    @Nullable
    public Integer getRole() {
        return role;
    }

    public void setState(@Nullable Integer state) {
        this.state = state;
    }

    @Nullable
    public Integer getState() {
        return state;
    }

    // Methodos obsoletos que serán incluidos en el Profile

    public void setUrl_image(String url_image) {
        if (profile == null)
            profile = new Profile();
        profile.setUrl_image(url_image);
    }

    public void setLastname(String lastname) {
        if (profile == null)
            profile = new Profile();
        profile.setLastname(lastname);
    }

    public void setNames(String names) {
        if (profile == null)
            profile = new Profile();
        profile.setNames(names);
    }

    @Nullable
    public String getUrl_image() {
        return (profile == null) ? null : profile.getUrl_image();
    }

    @Nullable
    public String getLastname() {
        return (profile == null) ? null : profile.getLastname();
    }

    @Nullable
    public String getNames() {
        return (profile == null) ? null : profile.getNames();
    }

    @NonNull
    @Exclude
    public final Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (profile != null) map.put("profile", profile.toMap());
        if (role != null) map.put("role", role);
        if (state != null) map.put("state", state);
        return map;
    }

    @NonNull
    @Exclude
    public static Map<String, Object> toMap(@NonNull Map<String, Member> memberMap) {
        Map<String, Object> map = new HashMap<>();
        for (String key : memberMap.keySet()){
            Member member = memberMap.get(key);
            if (member != null)
                map.put(key, member.toMap());
        }
        return map;
    }

}
