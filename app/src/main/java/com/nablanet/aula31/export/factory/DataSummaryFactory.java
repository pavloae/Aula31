package com.nablanet.aula31.export.factory;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.nablanet.aula31.export.data.DataParams;
import com.nablanet.aula31.export.data.DataSummaryImpl;
import com.nablanet.aula31.export.data.DataTrackImpl;
import com.nablanet.aula31.export.data.DataWorkImpl;
import com.nablanet.aula31.export.entity.CourseExport;
import com.nablanet.aula31.export.entity.MemberCourseExport;
import com.nablanet.aula31.repo.entity.Attendance;
import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.repo.entity.CourseWork;
import com.nablanet.documents.odf.content.spreadsheet.summary.DataSummary;

import java.util.ArrayList;
import java.util.List;

public class DataSummaryFactory {

    private CourseExport courseExport;

    private DataTrackImpl dataTrack;
    private List<DataWorkImpl> dataWorkList;
    private List<ClassDay> classDayList;

    private boolean courseExportCompleted, dataTrackCompleted, dataWorkCompleted, classDayCompleted;

    public void setCourseExport(CourseExport courseExport) {
        this.courseExport = courseExport;
        courseExportCompleted = true;
    }

    public void setDataTrack(@Nullable DataTrackImpl dataTrack) {
        this.dataTrack = dataTrack;
        dataTrackCompleted = true;
    }

    public void setDataWorkList(@Nullable List<DataWorkImpl> dataWorkList) {
        this.dataWorkList = dataWorkList;
        dataWorkCompleted = true;
    }

    public void setClassDayList(@Nullable List<ClassDay> classDayList) {
        this.classDayList = classDayList;
        classDayCompleted = true;
    }

    public boolean isComplete() {
        return courseExportCompleted && dataWorkCompleted && dataTrackCompleted && classDayCompleted;
    }

    @WorkerThread
    public DataSummaryImpl getDataSummary(@NonNull DataParams dataParams) {

        DataSummaryImpl dataSummary = new DataSummaryImpl();

        if (courseExport != null) {
            dataSummary.period = courseExport.period;
            dataSummary.subject = courseExport.subjectName;
            dataSummary.year = courseExport.subjectGrade;
            dataSummary.classroom = courseExport.classroom;
            dataSummary.teacher = dataParams.getTeacher();
        }

        if (dataParams.getMemberIdList() != null) {
            List<DataSummary.Member> memberList = new ArrayList<>();

            DataSummaryImpl.MemberImpl member;
            for (String memberId : dataParams.getMemberIdList()) {
                MemberCourseExport memberCourseExport = courseExport.getMember(memberId);
                if (memberCourseExport == null) continue;

                member = new DataSummaryImpl.MemberImpl();
                member.fullName = memberCourseExport.getFullName();
                member.attendance = getAttendace(memberId);
                member.rate = getRate(memberId);
                member.homeWork = getHomeWork(memberId);
                member.classWork = getClassWork(memberId);

                memberList.add(member);
            }

            dataSummary.members = memberList;

        }

        return dataSummary;
    }

    private Float getAttendace(String memberId) {
        int present = 0;
        int absent = 0;
        if (classDayList == null) return null;
        for (ClassDay classDay : classDayList) {
            if (classDay == null)
                continue;
            Boolean memberAttendance = isPresent(classDay, memberId);
            if (memberAttendance == null)
                continue;
            if (memberAttendance)
                present++;
            else
                absent++;
        }
        int total = present + absent;
        if (total == 0) return null;
        return present / (float) total;
    }

    private Boolean isPresent(ClassDay classDay, String memberId) {
        Attendance attendance;
        if (
                classDay.members == null
                        || !classDay.members.containsKey(memberId)
                        || (attendance = classDay.members.get(memberId)) == null
        )
            return null;
        return attendance.getPresent();
    }

    @Nullable
    private Integer getRate(String memberId) {

        if (dataTrack == null || dataTrack.getMembers() == null)
            return null;

        for (DataTrackImpl.MemberImpl member : dataTrack.getMembers()) {
            if (member.getMemberId().equals(memberId) && member.getListDay() != null) {
                int sum = 0;
                int count = 0;
                for (DataTrackImpl.DayImpl day : member.getListDay()) {
                    if (day.getRate() != null) {
                        sum += day.getRate();
                        count++;
                    }
                }
                return (count == 0) ? null : (int) (sum / (float) count);
            }
        }
        return null;
    }

    @Nullable
    private Integer getHomeWork(String memberId) {
        if (dataWorkList == null)
            return null;
        int sum = 0;
        int count = 0;
        for (DataWorkImpl dataWork : dataWorkList) {
            if (dataWork.isType(CourseWork.HOMEWORK) && dataWork.getMembers() != null)
                for (DataWorkImpl.MemberImpl member : dataWork.getMembers()) {
                    if (member.getMemberId() != null && member.getMemberId().equals(memberId)){
                        Integer note;
                        if ((note = member.getAverageNote()) != null) {
                            sum += note;
                            count++;
                        }
                    }
                }

        }
        if (count == 0) return null;
        return (int) (sum / (float) count);
    }

    @Nullable
    private Integer getClassWork(String memberId) {
        if (dataWorkList == null)
            return null;
        int sum = 0;
        int count = 0;
        for (DataWorkImpl dataWork : dataWorkList) {
            if (dataWork.isType(CourseWork.CLASSWORK) && dataWork.getMembers() != null)
                for (DataWorkImpl.MemberImpl member : dataWork.getMembers()) {
                    if (member.getMemberId() != null && member.getMemberId().equals(memberId)){
                        Integer note;
                        if ((note = member.getAverageNote()) != null) {
                            sum += note;
                            count++;
                        }
                    }
                }

        }
        if (count == 0) return null;
        return (int) (sum / (float) count);
    }

}
