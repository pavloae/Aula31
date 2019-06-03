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
        if (getInstitution() == null)
            return null;
        return getInstitution().getName();
    }

    @Exclude
    public String getSubjectName() {
        if (getSubject() == null)
            return null;
        return getSubject().getName();
    }

    @Exclude
    public String getCourseName() {
        return String.format(
                Locale.getDefault(),
                "%s - %s %s %s",
                (TextUtils.isEmpty(getSubjectName())) ? "[Materia]" : getSubjectName(),
                (getSubject().getGrade() == 0) ? "[año]" : getSubject().getGrade() + "º",
                (getClassroom() == null) ? "[aula]" : '"' + getClassroom() + '"',
                (getShift() == null) ? "[turno]" : "[" + getShift() + "]"
        );
    }

    @Exclude
    public int getSubjectGrade(){
        if (getSubject() == null || getSubject().getGrade() < 1 || getSubject().getGrade() > 9) return 0;
        return getSubject().getGrade();
    }

    @Exclude
    public void setInstitutionName(String institutionName) {
        if (getInstitution() == null)
            setInstitution(new Institution());
        getInstitution().setName(institutionName);
    }

    @Exclude
    public void setSubjectGrade(int subjectGrade) {
        if (getSubject() == null)
            setSubject(new Subject());
        getSubject().setGrade(subjectGrade);
    }

    @Exclude
    public void setSubjectName(String subjectName) {
        if (getSubject() == null)
            setSubject(new Subject());
        getSubject().setName(subjectName);
    }

}
