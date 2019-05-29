package com.nablanet.aula31.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

public class SnapshotToMap<T> {

    Class<T> clazz;

    public SnapshotToMap(@NonNull Class<T> clazz){
        this.clazz = clazz;
    }

    @Nullable
    public Map<String, T> from(@Nullable DataSnapshot dataSnapshot){
        if (dataSnapshot == null) return null;
        Map<String, T> map = new HashMap<>();
        for (DataSnapshot data : dataSnapshot.getChildren()) {
            if (data == null || data.getKey() == null) continue;
            T value = data.getValue(clazz);
            if (value != null)
                map.put(data.getKey(), value);
        }
        return map;
    }

}
