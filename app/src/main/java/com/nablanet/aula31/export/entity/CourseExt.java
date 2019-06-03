package com.nablanet.aula31.export.entity;

import com.nablanet.aula31.repo.entity.Course;
import com.nablanet.aula31.repo.entity.CourseProfile;
import com.nablanet.aula31.repo.entity.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CourseExt extends Course {

    public Integer period;

    public List<MemberCourseExport> memberCourseExportList;

    public CourseExt() {
        super();
    }

    public CourseExt(String key, CourseProfile profile, Map<String, Member> memberMap) {
        setKey(key);
        setProfile(profile);
        setMemberList(memberMap);
    }

    public void setMemberList(Map<String, Member> memberMap) {
        if (memberMap == null)
            memberCourseExportList = null;
        else {
            List<MemberCourseExport> memberCourseExportList = new ArrayList<>();
            Member member;
            for (String memberId : memberMap.keySet()) {
                if (memberId == null)
                    continue;
                if ((member = memberMap.get(memberId)) == null)
                    continue;
                if (member.getState() != Member.ACTIVE)
                    continue;
                memberCourseExportList.add(new MemberCourseExport(
                        memberId, member.getLastname(), member.getNames()
                ));
            }

            // Los miembros quedan ordenados por nombre
            Collections.sort(memberCourseExportList, new Comparator<MemberCourseExport>() {
                @Override
                public int compare(MemberCourseExport member1, MemberCourseExport member2) {
                    return member1.getFullName().compareTo(member2.getFullName());
                }
            });

            this.memberCourseExportList = memberCourseExportList;
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

    public MemberCourseExport getMember(String memberId) {
        for (MemberCourseExport member : memberCourseExportList)
            if (member.memberId.equals(memberId))
                return member;
        return null;
    }


}
