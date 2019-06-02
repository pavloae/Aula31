package com.nablanet.aula31.export.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nablanet.aula31.export.entity.CourseExt;

import java.util.List;

public class DataParams {

    private String courseId;
    private List<String> memberIdList;
    private Long from, to;
    private String teacher;

    private CourseExt courseExt;

    public DataParams() {
    }

    public DataParams(
            @NonNull CourseExt courseExt,
            @Nullable List<String> memberIdList,
            @Nullable Long from, @Nullable Long to
    ) {
        this.courseExt = courseExt;
        this.courseId = courseExt.courseId;
        this.memberIdList = memberIdList;
        this.from = from;
        this.to = to;
    }

    public void setCourseExt(CourseExt courseExt) {
        this.courseExt = courseExt;
    }

    public CourseExt getCourseExt() {
        return courseExt;
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
