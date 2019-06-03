package com.nablanet.aula31.repo.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CourseProfile extends KeyImpl {

    private Institution institution;
    private Subject subject;
    private String classroom;
    private Integer year;
    private String owner;
    private String shift;

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    @Exclude
    public final Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (institution != null) map.put("institution", institution.toMap());
        map.put("subject", subject.toMap());
        map.put("classroom", classroom);
        map.put("year", year);
        map.put("owner", owner);
        map.put("shift", shift);
        return map;
    }

}
