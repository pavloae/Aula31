package com.nablanet.documents.odf.content.spreadsheet.summary;

import com.nablanet.documents.odf.content.spreadsheet.DataSheet;

import java.util.List;

public interface DataSummary extends DataSheet {

    String getTitle();
    Integer getPeriod();
    String getSubject();
    Integer getYear();
    String getClassroom();
    String getTeacher();
    String getReferences();
    List<Member> getMembers();

    interface Member {
        String getFullName();
        Float getAttendance();
        Integer getRate();
        Integer getHomeWork();
        Integer getClassWork();
        String getObservation();
        Integer getNote();
    }

}
