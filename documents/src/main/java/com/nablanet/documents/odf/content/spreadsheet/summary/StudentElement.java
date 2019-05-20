package com.nablanet.documents.odf.content.spreadsheet.summary;

import com.nablanet.documents.odf.content.spreadsheet.RowElement;

import org.w3c.dom.Element;

public class StudentElement extends RowElement {

    public StudentElement(Element element) {
        super(element);
    }

    public void setValues(DataSummary.Member member) {
        getCell(1).setTextValue(member.getFullName());
        getCell(2).setPercentageValue(member.getAttendance());
        getCell(3).setIntegerValue(member.getRate());
        getCell(4).setIntegerValue(member.getHomeWork());
        getCell(5).setIntegerValue(member.getClassWork());
        getCell(6).setTextValue(member.getObservation());
        getCell(7).setIntegerValue(member.getNote());
    }

}
