package com.nablanet.documents.odf.content.spreadsheet.track;

import com.nablanet.documents.odf.content.spreadsheet.Sheet;

import org.w3c.dom.Element;

public class TrackSheet extends Sheet<DataTrack> {

    StudentHeaderElement headerFormat;
    StudentDayElement dayFormat;

    public TrackSheet(Element sheet) {
        super(sheet);
        headerFormat = new StudentHeaderElement(getRow(3).getElement());
        dayFormat = new StudentDayElement(getRow(4).getElement());
    }

    @Override
    public void setData(DataTrack data) {
        super.setData(data);

        StudentHeaderElement studentHeaderElement;
        for (DataTrack.Member member : data.getMembers()) {
            studentHeaderElement = new StudentHeaderElement(headerFormat.clone(true));
            studentHeaderElement.setValue(member.getName());
            insertBefore(studentHeaderElement, headerFormat);

            StudentDayElement dayElement;
            for (DataTrack.Day day : member.getListDay()) {
                dayElement = new StudentDayElement(dayFormat.clone(true));
                dayElement.setValues(day);
                insertBefore(dayElement, headerFormat);
            }
        }

        getElement().removeChild(dayFormat.getElement());
        getElement().removeChild(headerFormat.getElement());

    }
}
