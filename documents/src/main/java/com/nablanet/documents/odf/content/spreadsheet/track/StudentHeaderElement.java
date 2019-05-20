package com.nablanet.documents.odf.content.spreadsheet.track;

import com.nablanet.documents.odf.content.spreadsheet.RowElement;

import org.w3c.dom.Element;

public class StudentHeaderElement extends RowElement {

    public StudentHeaderElement(Element element) {
        super(element);
    }

    public void setValue(String studentName) {
        getCell(1).setTextValue(studentName);
    }

}
