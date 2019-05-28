package com.nablanet.documents.odf.content;

import com.nablanet.documents.odf.TemplateODS;
import com.nablanet.documents.odf.content.spreadsheet.Spreadsheet;
import com.nablanet.documents.odf.content.spreadsheet.summary.DataSummary;
import com.nablanet.documents.odf.content.spreadsheet.track.DataTrack;
import com.nablanet.documents.odf.content.spreadsheet.work.DataWork;
import com.nablanet.documents.utils.FileManager;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.TransformerException;

import static junit.framework.TestCase.assertNotNull;

public class BodyTest {

    @Test
    public void getSpreadsheet() throws TransformerException, IOException {

        File file = FileManager.getFile(TemplateODS.MODEL);
        assertNotNull(file);


        TemplateODS templateODS = new TemplateODS(new File(file.getParentFile(), "prueba.ods"));

        Spreadsheet spreadsheet = templateODS.getBody().getSpreadsheet();

        DataSummaryImpl dataSummary = new DataSummaryImpl(
                "RACING CAMPEON",
                4, "Geología", 5, "B", "ANDINO, Pablo E.",
                "Notas de referencias...",
                getStudents()
        );
        DataTrack dataTrack = new DataTrackImpl(getTrackStudent());
        List<DataWork> dataWorks = new ArrayList<>();
        dataWorks.add(
                new DataWorkImpl(
                        "Trabajo N°1",
                        "Individual", 1,
                        "Lenguaje coloquial y simbólico",
                        "Conocimiento", "Desarrollo", "Resultado",
                        getWorkStudent()
                )
        );
        dataWorks.add(
                new DataWorkImpl(
                        "Trabajo N°2",
                        "Grupal", 2,
                        "Álgebra",
                        "Conocimiento", "Participación", "Resultado",
                        getWorkStudent()
                )
        );


        spreadsheet.setData(dataSummary, dataTrack, dataWorks);

        templateODS.save();

    }

    @Test
    public void getAsElement() {
    }

    private List<DataSummary.Member> getStudents() {
        List<DataSummary.Member> students = new ArrayList<>();
        students.add(new MemberImpl(
                "ANDINO, Pablo Esteban", 0.02f, 3,4,
                5, "SEIS", 7)
        );
        students.add(new MemberImpl(
                "FERNANDEZ, Cristina Elizabeth", 0.44f, 5,6,
                7, "DOS", 8)
        );
        return students;
    }

    private List<DataTrack.Member> getTrackStudent() {
        DataTrack.Day track1 = new DayImpl(12765987L, 5, "cinco");
        DataTrack.Day track2 = new DayImpl(12762345L, 6, "seis");
        DataTrack.Day track3 = new DayImpl(12762355L, 7, "siete");
        DataTrack.Day track4 = new DayImpl(12734565L, 8, "ocho");
        DataTrack.Day track5 = new DayImpl(12574444L, 9, "nueve");

        List<DataTrack.Member> trackStudents = new ArrayList<>();
        trackStudents.add(new MemberTrackImpl("ANDINO, Pablo Esteban", track1, track2));
        trackStudents.add(new MemberTrackImpl("ZABALA, Marcela Roxana", track3, track4, track5));
        return trackStudents;
    }

    private List<DataWork.Member> getWorkStudent() {
        List<DataWork.Member> workMembers = new ArrayList<>();
        workMembers.add(new MemberWorkImpl(
                "TITO1", 127899436L,
                5, 6, 7, 6,
                "REHACER"
        ));
        workMembers.add(new MemberWorkImpl(
                "TITO2", 127599436L,
                6, 7, 8, 7,
                "APROBADO"
        ));
        return workMembers;
    }

    public class DataSummaryImpl implements DataSummary {

        public String title;
        public Integer period;
        public String subject;
        public Integer year;
        public String classroom;
        public String teacher;
        public String references;
        public List<Member> members;

        public DataSummaryImpl(String title, Integer period, String subject, Integer year,
                               String classroom, String teacher, String references,
                               List<Member> members) {
            this.title = title;
            this.period = period;
            this.subject = subject;
            this.year = year;
            this.classroom = classroom;
            this.teacher = teacher;
            this.references = references;
            this.members = members;
        }

        @Override
        public String getTitle() {
            return title;
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
            return "Summary";
        }
    }

    public class MemberImpl implements DataSummary.Member {

        public String name;
        public Float attendance;
        public Integer rate;
        public Integer homeWork;
        public Integer classWork;
        public String observation;
        public Integer note;

        public MemberImpl(String name, Float attendance, Integer rate, Integer homeWork,
                          Integer classWork, String observation, Integer note) {
            this.name = name;
            this.attendance = attendance;
            this.rate = rate;
            this.homeWork = homeWork;
            this.classWork = classWork;
            this.observation = observation;
            this.note = note;
        }

        @Override
        public String getFullName() {
            return name;
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
            return note;
        }
    }

    public class DataTrackImpl implements DataTrack {

        List<Member> members;

        public DataTrackImpl(List<Member> members) {
            this.members = members;
        }

        @Override
        public List<Member> getMembers() {
            return members;
        }

        @Override
        public String getSheetName() {
            return "Track";
        }
    }

    public class MemberTrackImpl implements DataTrack.Member {

        String name;
        List<DataTrack.Day> days;

        public MemberTrackImpl(String name, DataTrack.Day... days) {
            this.name = name;
            this.days = Arrays.asList(days);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<DataTrack.Day> getListDay() {
            return days;
        }

    }

    public class DayImpl implements DataTrack.Day {

        Long date;
        Integer rate;
        String comment;

        public DayImpl(Long date, Integer rate, String comment) {
            this.date = date;
            this.rate = rate;
            this.comment = comment;
        }

        @Override
        public Long getDate() {
            return date;
        }

        @Override
        public Integer getRate() {
            return rate;
        }

        @Override
        public String getComment() {
            return comment;
        }
    }

    public class DataWorkImpl implements DataWork {

        String title;
        String modality;
        Integer number;
        String content;
        String criteria1;
        String criteria2;
        String criteria3;
        List<Member> members;

        public DataWorkImpl(String title, String modality, Integer number, String content,
                            String criteria1, String criteria2, String criteria3,
                            List<Member> members) {
            this.title = title;
            this.modality = modality;
            this.number = number;
            this.content = content;
            this.criteria1 = criteria1;
            this.criteria2 = criteria2;
            this.criteria3 = criteria3;
            this.members = members;
        }

        @Override
        public String getName() {
            return title;
        }

        @Override
        public String getModality() {
            return modality;
        }

        @Override
        public Integer getNumber() {
            return number;
        }

        @Override
        public String getTopics() {
            return content;
        }

        @Override
        public String getCriteria1() {
            return criteria1;
        }

        @Override
        public String getCriteria2() {
            return criteria2;
        }

        @Override
        public String getCriteria3() {
            return criteria3;
        }

        @Override
        public List<Member> getMembers() {
            return members;
        }

        @Override
        public String getSheetName() {
            return "W"+number;
        }
    }

    public class MemberWorkImpl implements DataWork.Member {

        String name;
        Long date;
        Integer note1, note2, note3, averageNote;
        String observations;

        public MemberWorkImpl(String name, Long date,
                              Integer note1, Integer note2, Integer note3, Integer averageNote,
                              String observations) {
            this.name = name;
            this.date = date;
            this.note1 = note1;
            this.note2 = note2;
            this.note3 = note3;
            this.averageNote = averageNote;
            this.observations = observations;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Long getDate() {
            return date;
        }

        @Override
        public Integer getNote1() {
            return note1;
        }

        @Override
        public Integer getNote2() {
            return note2;
        }

        @Override
        public Integer getNote3() {
            return note3;
        }

        @Override
        public Integer getAverageNote() {
            return averageNote;
        }

        @Override
        public String getObservations() {
            return observations;
        }
    }

}