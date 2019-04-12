package com.nablanet.aula31.pojos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {

    public String uid;

    public String lastname = "";
    public String names = "";
    public String comment;
    public String urlImage;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name) {
        this.names = name;
    }

    public User(String lastname, String name){
        this.lastname = lastname;
        this.names = name;
    }

    public User setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public User setName(String name) {
        this.names = name;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



    public User(String nombre, String comment, String urlImage) {
        this.names = nombre;
        this.comment = comment;
        this.urlImage = urlImage;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lastname", lastname);
        result.put("names", names);
        result.put("comment", comment);
        result.put("url_image", urlImage);
        return result;
    }


}
