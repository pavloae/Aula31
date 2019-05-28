package com.nablanet.aula31.export.entity;

import com.google.firebase.database.Exclude;
import com.nablanet.aula31.repo.entity.Attendance;
import com.nablanet.aula31.repo.entity.ClassDay;

public class ClassDayExport extends ClassDay {

    @Exclude
    public Boolean isPresent(String memberId) {
        Attendance attendance;
        if (
                members == null
                        || !members.containsKey(memberId)
                        || (attendance = members.get(memberId)) == null
        )
            return null;
        return attendance.getPresent();
    }

}
