package com.nablanet.aula31.repo;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.nablanet.aula31.repo.entity.CourseProfile;
import com.nablanet.aula31.repo.entity.Subject;

import java.util.Locale;

public class Utils {

    public static String getCourseName(@NonNull CourseProfile courseProfile) {

        Subject subject = courseProfile.getSubject();
        String classroom = courseProfile.getClassroom();
        String shift = courseProfile.getShift();

        return String.format(
                Locale.getDefault(),
                "%s - %s %s %s",
                (subject == null || TextUtils.isEmpty(subject.getName())) ? "[Materia]" : subject.getName(),
                getSubjectGrade(subject) == 0 ? "[año]" : getSubjectGrade(subject) + "º",
                (classroom == null) ? "[aula]" : '"' + classroom + '"',
                (shift == null) ? "[turno]" : "[" + shift + "]"
        );

    }

    private static int getSubjectGrade(Subject subject){
        if (
                subject == null ||
                        subject.getGrade() == null ||
                        subject.getGrade() < 1 ||
                        subject.getGrade() > 9
        )
            return 0;

        return subject.getGrade();

    }

}
