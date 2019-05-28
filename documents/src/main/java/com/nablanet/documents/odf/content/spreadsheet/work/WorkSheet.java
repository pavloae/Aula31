package com.nablanet.documents.odf.content.spreadsheet.work;

import com.nablanet.documents.odf.content.spreadsheet.Sheet;

import org.w3c.dom.Element;

public class WorkSheet extends Sheet<DataWork> {

    StudentElement studentFormat;

    public WorkSheet(Element sheet) {
        super(sheet);
        studentFormat = new StudentElement(getRow(5).getElement());
    }

    @Override
    public void setData(DataWork data) {
        super.setData(data);

        getCell(1, 1).setTextValue(data.getName());
        getCell(2, 2).setTextValue(data.getModality());
        getCell(2, 4).setIntegerValue(data.getNumber());
        getCell(3, 2).setTextValue(data.getTopics());
        getCell(4, 3).setTextValue(data.getCriteria1());
        getCell(4, 4).setTextValue(data.getCriteria2());
        getCell(4, 5).setTextValue(data.getCriteria3());

        StudentElement studentElement;
        for (DataWork.Member member : data.getMembers()) {
            studentElement = new StudentElement(studentFormat.clone(true));
            studentElement.setValue(member);
            insertBefore(studentElement, studentFormat);
        }
        removeChild(studentFormat);

    }
}
