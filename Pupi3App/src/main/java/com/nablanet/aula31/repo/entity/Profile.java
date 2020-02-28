package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en
 * "/courses/$course_id/members/$member_id/profile/" fomando parte de {@link Member}
 *
 * "/tracking/$member_id/profile/" formando parte de {@link MemberTrack}
 *
 * "/works/$member_id/profile/" formando parte de {@link MemberRepo}
 */
public class Profile {

    @Nullable private String url_image;
    @Nullable private String lastname;
    @Nullable private String names;

    @Nullable
    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(@Nullable String url_image) {
        this.url_image = url_image;
    }

    @Nullable
    public String getLastname() {
        return lastname;
    }

    public void setLastname(@Nullable String lastname) {
        this.lastname = lastname;
    }

    @Nullable
    public String getNames() {
        return names;
    }

    public void setNames(@Nullable String names) {
        this.names = names;
    }

    @NonNull
    @Exclude
    public final Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (url_image != null) map.put("url_image", url_image);
        if (lastname != null) map.put("lastname", lastname);
        if (names != null) map.put("names", names);
        return map;
    }

}
