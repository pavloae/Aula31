package com.nablanet.aula31.classes;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.nablanet.aula31.courses.Course;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ClassDay {

    @Exclude
    public String id;

    public String course_id;
    public Long date;
    public String comment;
    public Map<String, Member> members;

    public Long getDate() {
        if (date == null)
            date = 0L;
        return date;
    }

    @Exclude
    public boolean isSameDay(ClassDay classDay) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTimeInMillis(date);
        cal2.setTimeInMillis(classDay.date);

        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);

    }

    public void prepareMembers(Map<String, Course.Member> memberCourseMap) {
        if (members == null) members = new HashMap<>();

        if (memberCourseMap == null) return;

        Member member;
        for (String courseMemberKey : memberCourseMap.keySet()) {
            if (!members.containsKey(courseMemberKey))
                members.put(courseMemberKey, new Member());
            if ((member = members.get(courseMemberKey)) == null)
                continue;
            member.setParams(courseMemberKey, memberCourseMap.get(courseMemberKey));
        }
    }

    public static class Member {

        @Exclude public String id;
        @Exclude public String url_image;
        @Exclude public String lastname;
        @Exclude public String names;

        public Boolean present;
        public Integer position;

        public Member(){}

        public Member(int position) {
            this.position = position;
        }

        public Member(String id, String url_image, String lastname, String names) {
            this.id = id;
            this.url_image = url_image;
            this.lastname = lastname;
            this.names = names;
        }

        @Exclude
        void setParams(String memberId, Course.Member member) {
            this.id = memberId;
            this.url_image = member.url_image;
            this.lastname = member.lastname;
            this.names = member.names;
        }

        @Exclude
        public String getFullName() {
            return String.format(Locale.getDefault(), "%s, %s", lastname, names);
        }

        @Exclude
        public boolean isNew(){
            return TextUtils.isEmpty(id);
        }

        @Override
        public int hashCode() {
            if (!TextUtils.isEmpty(id))
                return id.hashCode();
            else if (position != null)
                return position;
            else
                return super.hashCode();
        }
    }


}
