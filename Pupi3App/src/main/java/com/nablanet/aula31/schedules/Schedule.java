package com.nablanet.aula31.schedules;

import com.google.firebase.database.Exclude;

import java.util.Map;

public class Schedule {

    @Exclude
    public String id;

    public String course_id;
    public String user_id;
    public Map<String, Hours> weekdays;

    public static class Hours {

        @Exclude
        public String id;

        public int init;
        public int end;
    }

}
