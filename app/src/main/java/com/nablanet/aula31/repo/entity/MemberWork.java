package com.nablanet.aula31.repo.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class MemberWork {

    @Exclude public String work_id;

    public Long date;
    public String comment;
    public Integer criteria1;
    public Integer criteria2;
    public Integer criteria3;

    @Exclude
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        map.put("comment", comment);
        map.put("criteria1", criteria1);
        map.put("criteria2", criteria2);
        map.put("criteria3", criteria3);
        return map;
    }

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
