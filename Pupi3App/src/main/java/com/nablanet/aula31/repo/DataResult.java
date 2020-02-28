package com.nablanet.aula31.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Map;

public class DataResult<T> {

    private DataSnapshot dataSnapshot;
    private DatabaseError databaseError;
    private final SnapshotFactory<T> snapshotFactory;
    private Map<String, T> map;
    private T value;

    public DataResult(@NonNull DataSnapshot dataSnapshot, @NonNull SnapshotFactory<T> snapshotFactory) {
        this.dataSnapshot = dataSnapshot;
        this.snapshotFactory = snapshotFactory;
    }

    public DataResult(@NonNull DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
        this.snapshotFactory = null;
    }

    public DataResult(DatabaseError databaseError) {
        this.databaseError = databaseError;
        this.snapshotFactory = null;
    }

    public DataSnapshot getDataSnapshot() {
        return dataSnapshot;
    }

    public DatabaseError getDatabaseError() {
        return databaseError;
    }

    @Nullable
    @WorkerThread
    public Map<String, T> getMap() {
        if (map == null && snapshotFactory != null)
            map = snapshotFactory.toMap(dataSnapshot);
        return map;
    }

    public T getValue() {
        if (value == null && snapshotFactory != null)
            value = snapshotFactory.toValue(dataSnapshot);
        return value;
    }

}
