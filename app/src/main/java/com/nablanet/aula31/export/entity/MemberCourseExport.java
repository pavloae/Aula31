package com.nablanet.aula31.export.entity;

import com.google.firebase.database.Exclude;

import java.util.Locale;

public class MemberCourseExport {

    public String memberId;
    public String lastname;
    public String names;

    @Exclude
    public boolean checked;

    public MemberCourseExport(String memberId, String lastname, String names) {
        this.memberId = memberId;
        this.lastname = lastname;
        this.names = names;
    }

    public String getFullName() {
        return String.format(Locale.getDefault(), "%s, %s", lastname, names);
    }
}
