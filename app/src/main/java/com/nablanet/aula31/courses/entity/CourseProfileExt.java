package com.nablanet.aula31.courses.entity;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.nablanet.aula31.repo.entity.CourseProfile;
import com.nablanet.aula31.repo.entity.Institution;
import com.nablanet.aula31.repo.entity.Subject;

import java.util.Locale;

public class CourseProfileExt extends CourseProfile {

    @Exclude
    public String getInstitutionName() {
        if (institution == null) return "";
        return institution.name;
    }

    @Exclude
    public String getSubjectName() {
        if (subject == null || subject.name == null) return "";
        return subject.name;
    }

    @Exclude
    public String getCourseName() {
        return String.format(
                Locale.getDefault(),
                "%s - %s %s %s",
                (TextUtils.isEmpty(getSubjectName())) ? "[Materia]" : getSubjectName(),
                (subject.grade == 0) ? "[año]" : String.valueOf(subject.grade) + "º",
                (classroom == null) ? "[aula]" : '"' + classroom + '"',
                (shift == null) ? "[turno]" : "[" + shift + "]"
        );
    }

    @Exclude
    public int getSubjectGrade(){
        if (subject == null || subject.grade < 1 || subject.grade > 9) return 0;
        return subject.grade;
    }

    @Exclude
    public void setInstitutionName(String institutionName) {
        if (institution == null) institution = new Institution();
        institution.name = institutionName;
    }

    @Exclude
    public void setSubjectGrade(int subjectGrade) {
        if (subject == null) subject = new Subject();
        subject.grade = subjectGrade;
    }

    @Exclude
    public void setSubjectName(String subjectName) {
        if (subject == null) subject = new Subject();
        subject.name = subjectName;
    }

}
