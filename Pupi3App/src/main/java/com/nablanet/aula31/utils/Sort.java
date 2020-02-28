package com.nablanet.aula31.utils;

import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.repo.entity.ClassTrack;
import com.nablanet.aula31.repo.entity.Member;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sort {

    public static final int ASC = 0;
    public static final int DESC = 1;


    public static void byFullName(List<? extends Member> members) {

        if (members == null || members.size() <= 1)
            return;

        Collections.sort(members, new Comparator<Member>() {

            @Override
            public int compare(Member o1, Member o2) {

                if (o1.getLastname() == null)
                    return -1;

                if (o2.getLastname() == null)
                    return 1;

                int comp = o1.getLastname().compareTo(o2.getLastname());

                if (comp != 0)
                    return comp;

                if (o1.getNames() == null)
                    return -1;

                if (o2.getNames() == null)
                    return 1;

                return o1.getNames().compareTo(o2.getNames());
            }

        });

    }

    public static void byTrack(List<ClassTrack> classTrackList) {

        if (classTrackList == null || classTrackList.size() <= 1)
            return;

        // Las clases quedan ordenadas de la más antigua a la más reciente
        Collections.sort(classTrackList, new Comparator<ClassTrack>() {
            @Override
            public int compare(ClassTrack o1, ClassTrack o2) {
                if (o1.getDate() == null) return -1;
                if (o2.getDate() == null) return 1;
                return o1.getDate().compareTo(o2.getDate());
            }
        });

    }

    public static void byTrack(List<ClassTrack> classTrackList, int order){

        byTrack(classTrackList);

        if (order == DESC)
            Collections.reverse(classTrackList);

    }

    /** Ordena las clases por fecha {@link ClassDay}
     *
     * @param classDayList Una lista con clases para ser ordenada
     */
    public static void byDate(List<ClassDay> classDayList) {

        if (classDayList == null || classDayList.size() <= 1)
            return;

        // Las clases quedan ordenadas de la más antigua a la más reciente
        Collections.sort(classDayList, new Comparator<ClassDay>() {
            @Override
            public int compare(ClassDay o1, ClassDay o2) {
                if (o1.getDate() == null) return -1;
                if (o2.getDate() == null) return 1;
                return o1.getDate().compareTo(o2.getDate());
            }
        });

    }

    /** Ordena las clases por fecha {@link ClassDay}
     *
     * @param classDayList Una lista con clases para ser ordenada
     * @param order La forma de orden
     *              {@value ASC}: La más antigua primero,
     *              {@value DESC}: La más reciente primero
     */
    public static void byDate(List<ClassDay> classDayList, int order){

        byDate(classDayList);

        if (order == DESC)
            Collections.reverse(classDayList);

    }


}
