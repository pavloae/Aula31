package com.nablanet.aula31.classes.entity;

import android.support.annotation.NonNull;

public class ClassDayItem {

    public String classId;
    private Long date;
    public String comment;

    @NonNull
    public Long getDate() {
        if (date == null)
            date = 0L;
        return date;
    }

}
