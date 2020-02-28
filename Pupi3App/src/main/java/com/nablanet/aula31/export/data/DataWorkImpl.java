package com.nablanet.aula31.export.data;

import androidx.annotation.NonNull;

import com.nablanet.aula31.repo.entity.CourseWork;
import com.nablanet.documents.odf.content.spreadsheet.work.DataWork;

import java.util.List;

public class DataWorkImpl implements DataWork {

    Integer type;
    Integer number;
    String modality;
    String name;
    Long date;
    String topics;
    String criteria1;
    Integer val1;
    String criteria2;
    Integer val2;
    String criteria3;
    Integer val3;
    List<MemberImpl> members;

    public DataWorkImpl() {
    }

    public void setRepoWork(CourseWork repoWork) {
        this.type = repoWork.getType();
        this.number = repoWork.getNumber();
        this.modality = repoWork.getModality();
        this.name = repoWork.getName();
        this.date = repoWork.getDate();
        this.topics = repoWork.getTopics();
        if (repoWork.getCriteria1() != null) {
            this.criteria1 = repoWork.getCriteria1().getName();
            this.val1 = repoWork.getCriteria1().getWeight();
        }
        if (repoWork.getCriteria2() != null) {
            this.criteria2 = repoWork.getCriteria2().getName();
            this.val2 = repoWork.getCriteria2().getWeight();
        }
        if (repoWork.getCriteria3() != null) {
            this.criteria3 = repoWork.getCriteria3().getName();
            this.val3 = repoWork.getCriteria3().getWeight();
        }
    }

    public void setMembers(List<MemberImpl> members) {
        this.members = members;
    }

    public boolean isType(Integer type) {
        return this.type != null && this.type.equals(type);
    }

    @Override
    public String getName() {
        return name;
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
        return topics;
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
    public List<MemberImpl> getMembers() {
        return members;
    }

    @Override
    public String getSheetName() {
        return ((type != null && type == CourseWork.CLASSWORK) ? "C-" : "H-") + number;
    }

    public static class MemberImpl implements Member {

        String memberId;
        String name;
        Long date;
        Integer note1, note2, note3, averageNote;
        String observations;

        public MemberImpl(@NonNull String memberId, String name, Long date,
                          Integer note1, Integer note2, Integer note3, Integer averageNote,
                          String observations) {
            this.memberId = memberId;
            this.name = name;
            this.date = date;
            this.note1 = note1;
            this.note2 = note2;
            this.note3 = note3;
            this.averageNote = averageNote;
            this.observations = observations;
        }

        public String getMemberId() {
            return memberId;
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
