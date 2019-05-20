package com.nablanet.documents.odf.content.spreadsheet.track;

import com.nablanet.documents.odf.content.spreadsheet.RowElement;

import org.w3c.dom.Element;

public class StudentDayElement extends RowElement {

    public StudentDayElement(Element element) {
        super(element);
    }

    public void setValues(DataTrack.Day day) {
        getCell(1).setDate(day.getDate());
        getCell(2).setIntegerValue(day.getRate());
        getCell(3).setTextValue(day.getComment());
    }
}
