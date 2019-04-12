package com.nablanet.aula31.tracking;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberTrack {

    @Exclude
    String id;

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
            classTrack.id = classKey;
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


    public static class Profile {
        public String url_image;
        public String lastname;
        public String names;
    }

    public static class ClassTrack {

        @Exclude
        String id;

        public Long date;
        public Map<String, Observation> observations;

        @Exclude
        public Observation getObservation(String userId) {
            return (observations == null) ? null : observations.get(userId);
        }

    }

    public static class Observation {

        @Exclude
        String id;

        public Integer rate;
        public String comment;

        public Observation(){
        }

        public Observation(Integer rate, String comment) {
            this.rate = rate;
            this.comment = comment;
        }

    }

}
