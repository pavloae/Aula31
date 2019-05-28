package com.nablanet.aula31.export.data;

import com.nablanet.aula31.export.entity.MemberCourseExport;
import com.nablanet.documents.odf.content.spreadsheet.summary.DataSummary;

import java.util.ArrayList;
import java.util.List;

public class DataSummaryImpl implements DataSummary {

    public Integer period;
    public String subject;
    public Integer year;
    public String classroom;
    public String teacher;
    public String references;
    public List<Member> members;

    @Override
    public String getTitle() {
        return "Planilla de Resumen";
    }

    @Override
    public Integer getPeriod() {
        return period;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public Integer getYear() {
        return year;
    }

    @Override
    public String getClassroom() {
        return classroom;
    }

    @Override
    public String getTeacher() {
        return teacher;
    }

    @Override
    public String getReferences() {
        return references;
    }

    @Override
    public List<Member> getMembers() {
        return members;
    }

    @Override
    public String getSheetName() {
        return "Resumen";
    }

    public static class MemberImpl implements Member {

        public String fullName;
        public Float attendance;
        public Integer rate;
        public Integer homeWork;
        public Integer classWork;
        public String observation;

        @Override
        public String getFullName() {
            return fullName;
        }

        @Override
        public Float getAttendance() {
            return attendance;
        }

        @Override
        public Integer getRate() {
            return rate;
        }

        @Override
        public Integer getHomeWork() {
            return homeWork;
        }

        @Override
        public Integer getClassWork() {
            return classWork;
        }

        @Override
        public String getObservation() {
            return observation;
        }

        @Override
        public Integer getNote() {
            List<Integer> notes = new ArrayList<>();
            if (getRate() != null) notes.add(getRate());
            if (getHomeWork() != null) notes.add(getHomeWork());
            if (getClassWork() != null) notes.add(getClassWork());

            int sum = 0;
            for (Integer note : notes)
                sum += note;
            return (int) (sum / (float) notes.size());
        }
    }

}
