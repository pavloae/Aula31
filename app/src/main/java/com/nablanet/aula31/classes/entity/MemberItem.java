package com.nablanet.aula31.classes.entity;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nablanet.aula31.repo.entity.Attendance;
import com.nablanet.aula31.repo.entity.Member;
import com.nablanet.aula31.repo.entity.Profile;

import java.util.Locale;

public class MemberItem extends Member {

    public String courseId;
    public String classId;

    public String memberId;
    public String url_image;
    public String lastname;
    public String names;

    Attendance attendance;

    public MemberItem() {
    }

    public MemberItem(Integer position) {
        if (attendance == null)
            attendance = new Attendance();
        attendance.setPosition(position);
    }

    public boolean isNew(){
        return TextUtils.isEmpty(memberId);
    }

    public boolean isAtPupitre(int position) {
        return (
                attendance != null &&
                        attendance.getPosition() != null &&
                        attendance.getPosition() == position
        );
    }

    public void setMember(Member member) {
        this.memberId = member.member_id;
        setUser_id(member.getUser_id());
        setProfile(member.getProfile());
        setRole(member.getRole());
        setState(member.getState());
    }

    public Member getMember() {
        Member member = new Member();
        member.member_id = memberId;
        member.setUser_id(getUser_id());
        member.setProfile(getProfile());
        member.setRole(getRole());
        member.setState(getState());
        return member;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public Integer getPosition() {
        return (attendance == null || attendance.getPosition() == null) ?
                null : attendance.getPosition();
    }

    public void setPosition(Integer position) {
        if (attendance == null)
            attendance = new Attendance();
        attendance.setPosition(position);
    }

    public Boolean getPresent() {
        return (attendance == null || attendance.getPresent() == null) ?
                null : attendance.getPresent();
    }

    public void setPresent(Boolean present) {
        if (attendance == null)
            attendance = new Attendance();
        attendance.setPresent(present);
    }

    public String getFullName() {
        return String.format(Locale.getDefault(), "%s, %s", lastname, names);
    }

    @Override
    public int hashCode() {
        if (!TextUtils.isEmpty(memberId))
            return memberId.hashCode();
        else if (attendance != null && attendance.getPosition() != null)
            return attendance.getPosition();
        else
            return super.hashCode();
    }

}
