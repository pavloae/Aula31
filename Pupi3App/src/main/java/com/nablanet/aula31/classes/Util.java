package com.nablanet.aula31.classes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nablanet.aula31.classes.entity.MemberItem;
import com.nablanet.aula31.repo.entity.Attendance;
import com.nablanet.aula31.repo.entity.Member;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    @NonNull
    public static List<MemberItem> mergeLists(
            @Nullable List<Member> courseMembers, @Nullable Map<String, Attendance> attendanceMap
    ) {

        if (courseMembers == null)
            courseMembers = new ArrayList<>();

        if (attendanceMap == null)
            attendanceMap = new HashMap<>();

        //Creamos una lista de asistencia nueva
        List<MemberItem> mergedMemberList = new ArrayList<>();

        // Recorremos la lista de miembros del curso que debería
        // contener a todos los que están en la lista de asistencia de la clase
        for (Member courseMember : courseMembers)

            // Si el miembro se encuentra activo, o ya no está activo pero
            // fue listado en algún momento en la clase,
            // lo agregamos a la nueva lista
            if (courseMember != null &&
                    (courseMember.getState() != null && courseMember.getState() == Member.ACTIVE ||
                            attendanceMap.containsKey(courseMember.getKey()))) {
                MemberItem memberItem = new MemberItem();
                memberItem.setMember(courseMember);
                memberItem.setAttendance(attendanceMap.get(courseMember.getKey()));
                mergedMemberList.add(memberItem);
            }

        return mergedMemberList;

    }

    public static boolean isSameDay(Long date1, Long date2) {

        if (date1 == null || date2 == null)
            return false;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTimeInMillis(date1);
        cal2.setTimeInMillis(date2);

        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);

    }

}
