package com.nablanet.documents.odf.content.spreadsheet.summary;

import com.nablanet.documents.odf.content.spreadsheet.Sheet;

import org.w3c.dom.Element;

public class SummarySheet extends Sheet<DataSummary> {

    StudentElement studentFormat;

    public SummarySheet(Element element) {
        super(element);
        studentFormat = new StudentElement(getRow(5).getElement());
    }

    @Override
    public void setData(DataSummary data) {
        super.setData(data);



        getCell(1, 1).setTextValue(data.getTitle());
        getCell(2, 2).setIntegerValue(data.getPeriod());
        getCell(2, 4).setTextValue(data.getSubject());
        getCell(2, 6).setIntegerValue(data.getYear());
        getCell(2, 8).setTextValue(data.getClassroom());
        getCell(2, 10).setTextValue(data.getTeacher());
        getCell(3, 1).setTextValue(data.getReferences());

        StudentElement studentElement;
        for (DataSummary.Member member : data.getMembers()) {
            studentElement = new StudentElement(studentFormat.clone(true));
            studentElement.setValues(member);
            insertBefore(studentElement, studentFormat);
        }
        removeChild(studentFormat);

    }

}
