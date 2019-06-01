package com.nablanet.aula31.export.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nablanet.aula31.export.entity.CourseExport;

import java.util.List;

public class DataParams {

    private String courseId;
    private List<String> memberIdList;
    private Long from, to;
    private String teacher;

    private CourseExport courseExport;

    public DataParams() {
    }

    public DataParams(
            @NonNull CourseExport courseExport,
            @Nullable List<String> memberIdList,
            @Nullable Long from, @Nullable Long to
    ) {
        this.courseExport = courseExport;
        this.courseId = courseExport.courseId;
        this.memberIdList = memberIdList;
        this.from = from;
        this.to = to;
    }

    public void setCourseExport(CourseExport courseExport) {
        this.courseExport = courseExport;
    }

    public CourseExport getCourseExport() {
        return courseExport;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setMemberIdList(List<String> memberIdList) {
        this.memberIdList = memberIdList;
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

    public void setFrom(Long from) {
        this.from = from;
    }

    public void setTo(Long to) {
        this.to = to;
    }
}
