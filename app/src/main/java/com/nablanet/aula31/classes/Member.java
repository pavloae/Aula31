package com.nablanet.aula31.classes;

import com.google.firebase.database.Exclude;

class Member {

    @Exclude
    String id;

    public String course_id;
    public String url_image;
    public String lastname;
    public String names;
    public Boolean active;

    public Member() {}

    public Member(String lastname, String names) {
        this.lastname = lastname;
        this.names = names;
    }
}
