package com.nablanet.documents.odf.content.spreadsheet.track;

import com.nablanet.documents.odf.content.spreadsheet.DataSheet;

import java.util.List;

public interface DataTrack extends DataSheet {

    List<? extends Member> getMembers();

    interface Member {
        String getName();
        List<? extends Day> getListDay();
    }

    interface Day {
        Long getDate();
        Integer getRate();
        String getComment();
    }

}
