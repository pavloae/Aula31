package com.nablanet.aula31.export.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public class DataParams {

    private String courseId;
    private List<String> memberIdList;
    private Long from, to;
    private String teacher;

    public DataParams(
            @NonNull String courseId,
            @Nullable List<String> memberIdList,
            @Nullable Long from, @Nullable Long to
    ) {
        this.courseId = courseId;
        this.memberIdList = memberIdList;
        this.from = from;
        this.to = to;
    }

    public String getCourseId() {
        return courseId;
    }

    @Nullable
    public List<String> getMemberIdList() {
        return memberIdList;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
