package com.nablanet.aula31.tracking;

import com.google.firebase.database.Exclude;

import java.util.Map;

public class MemberTracking {

    @Exclude
    String memberId;

    String url_image;
    String lastname;
    String names;
    Map<String, ClassTracking> classes;

    public static class ClassTracking {



    }

}
