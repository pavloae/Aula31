package com.nablanet.aula31.classes.entity;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.nablanet.aula31.repo.entity.Attendance;
import com.nablanet.aula31.repo.entity.Member;

import java.util.Locale;

public class MemberItem extends Member {

    @Exclude
    private String courseId;
    @Exclude
    private String classId;
    @Exclude
    private Attendance attendance;

    public MemberItem() {
    }

    public MemberItem(Integer position) {
        attendance = new Attendance();
        attendance.setPosition(position);
    }

    @Exclude
    public String getCourseId() {
        return courseId;
    }

    @Exclude
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Exclude
    public String getClassId() {
        return classId;
    }

    @Exclude
    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Exclude
    public boolean isNew(){
        return TextUtils.isEmpty(getKey());
    }

    @Exclude
    public boolean isAtPupitre(int position) {
        return (
                attendance != null &&
                        attendance.getPosition() != null &&
                        attendance.getPosition() == position
        );
    }

    @Exclude
    public void setMember(@NonNull Member member) {
        setKey(member.getKey());
        setUser_id(member.getUser_id());
        setProfile(member.getProfile());
        setRole(member.getRole());
        setState(member.getState());
    }

    @Exclude
    public Member getMember() {
        Member member = new Member();
        member.setKey(getKey());
        member.setUser_id(getUser_id());
        member.setProfile(getProfile());
        member.setRole(getRole());
        member.setState(getState());
        return member;
    }

    @Exclude
    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    @Exclude
    public Attendance getAttendance() {
        return attendance;
    }

    @Exclude
    public Integer getPosition() {
        return (attendance == null || attendance.getPosition() == null) ?
                null : attendance.getPosition();
    }

    @Exclude
    public void setPosition(Integer position) {
        if (position != null && position < 0)
            position = null;
        if (attendance == null)
            attendance = new Attendance();
        attendance.setPosition(position);
    }

    @Exclude
    public Boolean getPresent() {
        return (attendance == null || attendance.getPresent() == null) ?
                null : attendance.getPresent();
    }

    @Exclude
    public void setPresent(Boolean present) {
        if (attendance == null)
            attendance = new Attendance();
        attendance.setPresent(present);
    }

    @Exclude
    public String getFullName() {
        if (getProfile() == null)
            return "";
        return String.format(
                Locale.getDefault(),
                "%s, %s",
                (getProfile().getLastname() == null) ? "" : getProfile().getLastname(),
                (getProfile().getNames() == null) ? "" : getProfile().getNames()
        );
    }

    @Exclude
    @Override
    public int hashCode() {
        if (!TextUtils.isEmpty(getKey()))
            return getKey().hashCode();
        else if (attendance != null && attendance.getPosition() != null)
            return attendance.getPosition();
        else
            return super.hashCode();
    }

}
