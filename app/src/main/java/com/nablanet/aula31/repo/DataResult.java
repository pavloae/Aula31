package com.nablanet.aula31.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.nablanet.aula31.repo.entity.Key;

import java.util.Map;

public class DataResult<T extends Key> {

    private DataSnapshot dataSnapshot;
    private DatabaseError databaseError;
    private SnapshotFactory<T> snapshotFactory;
    private Map<String, T> map;
    private T value;

    public DataResult(@NonNull DataSnapshot dataSnapshot, @NonNull SnapshotFactory<T> snapshotFactory) {
        this.dataSnapshot = dataSnapshot;
        this.snapshotFactory = snapshotFactory;
    }

    public DataResult(DatabaseError databaseError) {
        this.databaseError = databaseError;
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
        if (map == null)
            map = snapshotFactory.toMap(dataSnapshot);
        return map;
    }

    public T getValue() {
        if (value == null)
            value = snapshotFactory.toValue(dataSnapshot);
        return value;
    }

}
