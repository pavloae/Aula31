package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/works/$member_id/works/$work_id/"
 * Forma parte de la clase {@link MemberRepo}
 */
public class MemberWork extends KeyImpl {

    @Nullable private Long date;
    @Nullable private String comment;
    @Nullable private Integer criteria1;
    @Nullable private Integer criteria2;
    @Nullable private Integer criteria3;

    @Nullable
    public Long getDate() {
        return date;
    }

    public void setDate(@Nullable Long date) {
        this.date = date;
    }

    @Nullable
    public String getComment() {
        return comment;
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    @Nullable
    public Integer getCriteria1() {
        return criteria1;
    }

    public void setCriteria1(@Nullable Integer criteria1) {
        this.criteria1 = criteria1;
    }

    @Nullable
    public Integer getCriteria2() {
        return criteria2;
    }

    public void setCriteria2(@Nullable Integer criteria2) {
        this.criteria2 = criteria2;
    }

    @Nullable
    public Integer getCriteria3() {
        return criteria3;
    }

    public void setCriteria3(@Nullable Integer criteria3) {
        this.criteria3 = criteria3;
    }

    @NonNull
    @Exclude
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (date != null) map.put("date", date);
        if (comment != null) map.put("comment", comment);
        if (criteria1 != null) map.put("criteria1", criteria1);
        if (criteria2 != null) map.put("criteria2", criteria2);
        if (criteria3 != null) map.put("criteria3", criteria3);
        return map;
    }

    @NonNull
    @Exclude
    public static  Map<String, Object> toMap(@NonNull Map<String, MemberWork> memberWorkMap) {
        Map<String, Object> map = new HashMap<>();
        for (String key : memberWorkMap.keySet()){
            MemberWork memberWork = memberWorkMap.get(key);
            if (memberWork != null)
                map.put(key, memberWork.toMap());
        }
        return map;
    }

}
