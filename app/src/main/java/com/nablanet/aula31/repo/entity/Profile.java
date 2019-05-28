package com.nablanet.aula31.repo.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Profile {

    public String url_image;
    public String lastname;
    public String names;

    public Profile() {
    }

    public Profile(String url_image, String lastname, String names) {
        this.url_image = url_image;
        this.lastname = lastname;
        this.names = names;
    }

    @Exclude
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("url_image", url_image);
        map.put("lastname", lastname);
        map.put("names", names);
        return map;
    }

}
