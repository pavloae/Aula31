package com.nablanet.aula31.utils;

import com.nablanet.aula31.repo.entity.ClassDay;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sort {

    public static final int ASC = 0;
    public static final int DESC = 1;


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
