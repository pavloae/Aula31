package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/courses/$course_id/profile/"
 * Forma parte de la clase {@link Course}
 */
public class CourseProfile {

    @Nullable private Institution institution;
    @Nullable private Subject subject;
    @Nullable private String classroom;
    @Nullable private Integer year;
    @Nullable private String owner;
    @Nullable private String shift;

    @Nullable
    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(@Nullable Institution institution) {
        this.institution = institution;
    }

    @Nullable
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(@Nullable Subject subject) {
        this.subject = subject;
    }

    @Nullable
    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(@Nullable String classroom) {
        this.classroom = classroom;
    }

    @Nullable
    public Integer getYear() {
        return year;
    }

    public void setYear(@Nullable Integer year) {
        this.year = year;
    }

    @Nullable
    public String getOwner() {
        return owner;
    }

    public void setOwner(@Nullable String owner) {
        this.owner = owner;
    }

    @Nullable
    public String getShift() {
        return shift;
    }

    public void setShift(@Nullable String shift) {
        this.shift = shift;
    }

    /// Legacy

    public String getCourseName() {
        if (subject != null)
            return subject.getName();
        return null;
    }

    public void setCourseName(String courseName) {
        if (subject == null)
            subject = new Subject();
        //subject.setName(courseName);
    }

    public Integer getSubjectGrade() {
        if (subject != null)
            return subject.getGrade();
        return null;
    }

    public void setSubjectGrade(Integer subjectGrade) {
        if (subject == null)
            subject = new Subject();
        subject.setGrade(subjectGrade);
    }

    public String getSubjectName() {
        if (subject != null)
            return subject.getName();
        return null;
    }

    public void setSubjectName(String subjectName) {
        if (subject == null)
            subject = new Subject();
        subject.setName(subjectName);
    }

    public String getInstitutionName() {
        if (institution != null)
            return institution.getName();
        return null;
    }

    public void setInstitutionName(String institutionName) {
        if (institution == null)
            institution = new Institution();
        institution.setName(institutionName);
    }

    ///////

    @NonNull
    @Exclude
    public final Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (institution != null) map.put("institution", institution.toMap());
        if (subject != null) map.put("subject", subject.toMap());
        if (classroom != null) map.put("classroom", classroom);
        if (year != null) map.put("year", year);
        if (owner != null) map.put("owner", owner);
        if (shift != null) map.put("shift", shift);
        return map;
    }

}
