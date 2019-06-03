package com.nablanet.aula31.repo.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Profile extends KeyImpl {

    private String url_image;
    private String lastname;
    private String names;

    public Profile() {
    }

    public Profile(String url_image, String lastname, String names) {
        this.url_image = url_image;
        this.lastname = lastname;
        this.names = names;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    @Exclude
    final Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("url_image", url_image);
        map.put("lastname", lastname);
        map.put("names", names);
        return map;
    }

}
