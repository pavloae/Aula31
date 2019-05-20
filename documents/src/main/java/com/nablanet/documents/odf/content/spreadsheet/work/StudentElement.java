package com.nablanet.documents.odf.content.spreadsheet.work;

import com.nablanet.documents.odf.content.spreadsheet.RowElement;

import org.w3c.dom.Element;

public class StudentElement extends RowElement {

    public StudentElement(Element element) {
        super(element);
    }

    public void setValue(DataWork.Member member) {
        getCell(1).setTextValue(member.getName());
        getCell(2).setDate(member.getDate());
        getCell(3).setIntegerValue(member.getNote1());
        getCell(4).setIntegerValue(member.getNote2());
        getCell(5).setIntegerValue(member.getNote3());
        getCell(6).setIntegerValue(member.getAverageNote());
        getCell(7).setTextValue(member.getObservations());
    }

}
