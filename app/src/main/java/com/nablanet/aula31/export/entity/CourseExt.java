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

    public String institutionName;
    public String subjectName;
    public Integer subjectGrade;
    public String classroom;
    public Integer period;

    public List<MemberCourseExport> memberCourseExportList;

    public CourseExt() {
        super();
    }

    public CourseExt(String courseId, CourseProfile profile, Map<String, Member> memberMap) {
        this.courseId = courseId;
        setProfile(profile);
        setMemberList(memberMap);
    }

    public void setProfile(CourseProfile profile) {

        if (profile == null) {

            institutionName = null;
            subjectName = null;
            subjectGrade = null;
            classroom = null;

        } else  {

            if (profile.institution != null)
                institutionName = profile.institution.name;

            if (profile.subject != null) {
                subjectName = profile.subject.name;
                subjectGrade = profile.subject.grade;
            }

            classroom = profile.classroom;

        }

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

    public String getSubjectFullName() {
        return String.format(
                Locale.getDefault(), "%s %s%s", subjectName, subjectGrade, classroom
        );
    }

    public MemberCourseExport getMember(String memberId) {
        for (MemberCourseExport member : memberCourseExportList)
            if (member.memberId.equals(memberId))
                return member;
        return null;
    }


}
