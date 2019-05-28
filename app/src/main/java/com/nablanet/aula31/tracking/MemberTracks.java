package com.nablanet.aula31.tracking;

import com.google.firebase.database.Exclude;
import com.nablanet.aula31.repo.entity.ClassTrack;
import com.nablanet.aula31.repo.entity.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberTracks {

    @Exclude String id;

    public String course_id;
    public Profile profile;
    public Map<String, ClassTrack> classes;

    @Exclude
    public List<ClassTrack> getClassTrackList() {
        List<ClassTrack> classTrackList = new ArrayList<>();
        if (classes == null) classes = new HashMap<>();
        ClassTrack classTrack;
        for (String classKey : classes.keySet()) {
            classTrack = classes.get(classKey);
            classTrack.class_id = classKey;
            classTrackList.add(classTrack);
        }
        return classTrackList;
    }

    @Exclude
    public Float getAverageRate(String userId) {

        int count = 0;
        Float totalRate = 0F;

        for (ClassTrack classTrack : getClassTrackList())
            if (classTrack.getObservation(userId) != null){
                count++;
                totalRate += classTrack.getObservation(userId).rate;
            }

        return totalRate / count;
    }




}
