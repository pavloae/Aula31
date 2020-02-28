package com.nablanet.aula31.export.entity;

import com.nablanet.aula31.repo.entity.Course;
import com.nablanet.aula31.repo.entity.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CourseExt extends Course {

    public Integer period;
    List<MemberExt> memberExtList;

    public CourseExt(Course course) {
        setKey(course.getKey());
        setProfile(course.getProfile());
        setMembers(course.getMembers());
    }

    @Override
    public void setMembers(Map<String, Member> members) {
        super.setMembers(members);

        if (members == null)
            memberExtList = null;
        else {
            List<MemberExt> memberExtList = new ArrayList<>();
            Member member;
            for (String memberId : members.keySet()) {
                if (memberId == null)
                    continue;
                if ((member = members.get(memberId)) == null)
                    continue;
                if (member.getState() == null || member.getState() != Member.ACTIVE)
                    continue;
                memberExtList.add(new MemberExt(member));
            }

            // Los miembros quedan ordenados por nombre
            Collections.sort(memberExtList, new Comparator<MemberExt>() {
                @Override
                public int compare(MemberExt member1, MemberExt member2) {
                    return member1.getFullName().compareTo(member2.getFullName());
                }
            });

            this.memberExtList = memberExtList;
        }

    }

    public String getInstitutionName() {
        if (getProfile() == null || getProfile().getInstitution() == null)
            return null;
        return getProfile().getInstitution().getName();
    }

    public String getSubjectFullName() {
        return String.format(
                Locale.getDefault(), "%s %s%s",
                getSubjectName(), getSubjectGrade(), getClassroom()
        );
    }

    public String getSubjectName() {
        if (getProfile() == null || getProfile().getSubject() == null)
            return null;
        return getProfile().getSubject().getName();
    }

    public Integer getSubjectGrade() {
        if (getProfile() == null || getProfile().getSubject() == null)
            return null;
        return getProfile().getSubject().getGrade();
    }

    public String getClassroom() {
        if (getProfile() == null)
            return null;
        return getProfile().getClassroom();
    }

    public MemberExt getMember(String memberId) {
        for (MemberExt member : memberExtList)
            if (member.getKey() != null && member.getKey().equals(memberId))
                return member;
        return null;
    }

    public List<MemberExt> getMemberExtList() {
        return memberExtList;
    }
}
