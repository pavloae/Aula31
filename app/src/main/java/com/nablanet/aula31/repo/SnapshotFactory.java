package com.nablanet.aula31.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

public class SnapshotFactory<T> {

    Class<T> clazz;

    public SnapshotFactory(@NonNull Class<T> clazz){
        this.clazz = clazz;
    }

    @Nullable
    public Map<String, T> toMap(@Nullable DataSnapshot dataSnapshot){
        if (dataSnapshot == null) return null;
        Map<String, T> map = new HashMap<>();
        for (DataSnapshot data : dataSnapshot.getChildren()) {
            if (data == null || data.getKey() == null) continue;
            T value = toValue(dataSnapshot);
            if (value != null)
                map.put(data.getKey(), value);
        }
        return map;
    }

    @Nullable
    public T toValue(@Nullable DataSnapshot dataSnapshot) {
        if (dataSnapshot == null)
            return null;
        return dataSnapshot.getValue(clazz);
    }

}
