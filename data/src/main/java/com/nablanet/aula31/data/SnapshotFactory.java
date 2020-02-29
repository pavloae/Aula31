package com.nablanet.aula31.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.nablanet.aula31.core.repository.entity.Key;

import java.util.HashMap;
import java.util.Map;

public class SnapshotFactory<T> {

    private final Class<T> clazz;

    public SnapshotFactory(@NonNull Class<T> clazz){
        this.clazz = clazz;
    }

    @Nullable
    public Map<String, T> toMap(@Nullable DataSnapshot dataSnapshot){
        if (dataSnapshot == null) return null;
        Map<String, T> map = new HashMap<>();
        for (DataSnapshot data : dataSnapshot.getChildren()) {
            if (data == null || data.getKey() == null)
                continue;
            T value = toValue(data);
            if (value == null)
                continue;
            if (value instanceof Key)
                ((Key) value).setKey(data.getKey());
            map.put(data.getKey(), value);
        }
        return map;
    }

    @Nullable
    public T toValue(@Nullable DataSnapshot dataSnapshot) {
        if (dataSnapshot == null)
            return null;
        T value = dataSnapshot.getValue(clazz);
        if (value instanceof Key)
            ((Key) value).setKey(dataSnapshot.getKey());
        return value;
    }

}
