package com.nablanet.aula31.export.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nablanet.documents.odf.content.spreadsheet.track.DataTrack;

import java.util.Arrays;
import java.util.List;

public class DataTrackImpl implements DataTrack {

    List<MemberImpl> members;

    public DataTrackImpl(List<MemberImpl> members) {
        this.members = members;
    }

    @Override
    public List<MemberImpl> getMembers() {
        return members;
    }

    @Override
    public String getSheetName() {
        return "Track";
    }

    public static class MemberImpl implements Member {

        private String memberId;
        String name;
        List<DayImpl> days;

        public MemberImpl(@NonNull String memberId, String name, @Nullable DayImpl... days) {
            this.memberId = memberId;
            this.name = name;
            if (days != null && days.length > 0)
                this.days = Arrays.asList(days);
        }

        public MemberImpl(@NonNull String memberId, String name, List<DayImpl> days) {
            this.memberId = memberId;
            this.name = name;
            if (days != null && days.size() > 0)
                this.days = days;
        }

        public String getMemberId() {
            return memberId;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<DayImpl> getListDay() {
            return days;
        }

    }

    public static class DayImpl implements Day {

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

}
