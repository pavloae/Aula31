package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/tracking/$member_id/classes/$class_id/"
 * forma parte de la clase {@link MemberTrack}
 */
public class ClassTrack extends KeyImpl {

    @Nullable private Long date;
    @Nullable private Map<String, Observation> observations;

    @Nullable
    public Long getDate() {
        return date;
    }

    public void setDate(@Nullable Long date) {
        this.date = date;
    }

    @Nullable
    public Map<String, Observation> getObservations() {
        return observations;
    }

    public void setObservations(@Nullable Map<String, Observation> observations) {
        this.observations = observations;
    }

    @Exclude
    @Nullable
    public Observation getObservation(@NonNull String userId) {
        return (observations == null) ? null : observations.get(userId);
    }

    @Exclude
    @NonNull
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (date != null) map.put("date", date);
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
