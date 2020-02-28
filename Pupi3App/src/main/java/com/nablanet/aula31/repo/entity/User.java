package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/users/$uid/"
 */
public class User extends KeyImpl {

    @Nullable private String lastname;
    @Nullable private String names;
    @Nullable private String comment;
    @Nullable private String url_image;

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

    @Nullable
    public String getComment() {
        return comment;
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    @Nullable
    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(@Nullable String url_image) {
        this.url_image = url_image;
    }

    @NonNull
    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        if (lastname != null) result.put("lastname", lastname);
        if (names!= null) result.put("names", names);
        if (comment != null) result.put("comment", comment);
        if (url_image != null) result.put("url_image", url_image);
        return result;
    }

}
