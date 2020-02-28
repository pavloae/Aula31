package com.nablanet.aula31.export.data;

import androidx.annotation.Nullable;

import com.nablanet.aula31.export.entity.CourseExt;

import java.util.List;

public class DataParams {

    private String courseId;
    private List<String> memberIdList;
    private Long from, to;
    private String teacher;

    private CourseExt courseExt;

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

    public void setFrom(Long from) {
        this.from = from;
    }

    public void setTo(Long to) {
        this.to = to;
    }
}
