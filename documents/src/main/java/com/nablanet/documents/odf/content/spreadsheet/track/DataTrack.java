package com.nablanet.documents.odf.content.spreadsheet.track;

import com.nablanet.documents.odf.content.spreadsheet.DataSheet;

import java.util.List;

public interface DataTrack extends DataSheet {

    List<Member> getMembers();

    interface Member {
        String getName();
        List<Day> getListDay();
    }

    interface Day {

        Long getDate();
        Integer getRate();
        String getComment();

    }

}
