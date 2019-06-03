package com.nablanet.aula31.repo.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ClassTrack extends KeyImpl {

    public Long date;
    public Map<String, Observation> observations;

    @Exclude
    @Nullable
    public Observation getObservation(@NonNull String userId) {
        return (observations == null) ? null : observations.get(userId);
    }

    @Exclude
    @NonNull
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        if (observations != null) map.put("observations", Observation.toMap(observations));
        return map;
    }

    @Exclude
    @NonNull
    public static  Map<String, Object> toMap(@NonNull Map<String, ClassTrack> memberTrackMap) {
        Map<String, Object> map = new HashMap<>();
        for (String key : memberTrackMap.keySet()){
            ClassTrack classTrack = memberTrackMap.get(key);
            if (classTrack != null)
                map.put(key, classTrack.toMap());
        }
        return map;
    }

}
