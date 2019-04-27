package com.nablanet.aula31.courses;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Course {

    @Exclude
    public String id;

    public Profile profile;
    public Map<String, Member> members;

    public static class Profile {

        @Exclude
        public String courseId;

        public Institution institution;
        public Subject subject;
        public String classroom;
        public Integer year;
        public String owner;
        public String shift;

        public Profile(){}

        Profile(
                String institutionName,
                String subjectName, Integer subjectGrade,
                String classroom, Integer year, String owner, String shift
        ) {
            this.institution = new Institution();
            institution.name = institutionName;
            this.subject = new Subject();
            subject.name = subjectName;
            subject.grade = subjectGrade;
            this.classroom = classroom;
            this.year = year;
            this.owner = owner;
            this.shift = shift;
        }

        public String getInstitutionName() {
            if (institution == null) return "";
            return institution.name;
        }

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

        public int getSubjectGrade(){
            if (subject == null || subject.grade < 1 || subject.grade > 9) return 0;
            return subject.grade;
        }

        public void setInstitutionName(String institutionName) {
            if (institution == null) institution = new Institution();
            institution.name = institutionName;
        }

        public void setSubjectGrade(int subjectGrade) {
            if (subject == null) subject = new Subject();
            subject.grade = subjectGrade;
        }

        public void setSubjectName(String subjectName) {
            if (subject == null) subject = new Subject();
            subject.name = subjectName;
        }

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("institution", institution.toMap());
            map.put("subject", subject.toMap());
            map.put("classroom", classroom);
            map.put("year", year);
            map.put("owner", owner);
            map.put("shift", shift);
            return map;
        }

    }

    public static class Member {

        @Exclude public static final int INACTIVE = 0;
        @Exclude public static final int PENDING = 1;
        @Exclude public static final int ACTIVE = 2;

        @Exclude
        public String id;

        @Exclude
        public int presents, absents;

        public String user_id;
        public String url_image;
        public String lastname;
        public String names;
        public Integer role;
        public Integer state;

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", user_id);
            map.put("url_image", url_image);
            map.put("lastname", lastname);
            map.put("names", names);
            map.put("role", role);
            map.put("state", state);
            return map;
        }

    }

    public static class Institution {
        public String id;
        public String name;
        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            return map;
        }
    }

    public static class Subject {
        public String id;
        public String name;
        public int grade;
        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("grade", grade);
            return map;
        }
    }

    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("profile", profile.toMap());

        if (members != null)
            for (String key : members.keySet()){
                Member member = members.get(key);
                if (member != null)
                    map.put("members/" + key, member.toMap());
            }

        return map;
    }

}
