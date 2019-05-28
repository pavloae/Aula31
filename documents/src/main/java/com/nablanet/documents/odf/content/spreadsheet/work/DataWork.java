package com.nablanet.documents.odf.content.spreadsheet.work;

import com.nablanet.documents.odf.content.spreadsheet.DataSheet;

import java.util.List;

public interface DataWork extends DataSheet {

    String getName();
    String getModality();
    Integer getNumber();
    String getTopics();
    String getCriteria1();
    String getCriteria2();
    String getCriteria3();

    List<? extends Member> getMembers();

    interface Member {
        String getName();
        Long getDate();
        Integer getNote1();
        Integer getNote2();
        Integer getNote3();
        Integer getAverageNote();
        String getObservations();
    }

}
