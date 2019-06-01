package com.nablanet.aula31.repo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Map;

public class DataResult<T> {

    private DataSnapshot dataSnapshot;
    private DatabaseError databaseError;
    private SnapshotToMap<T> snapshotToMap;
    private Map<String, T> map;

    public DataResult(@NonNull DataSnapshot dataSnapshot, @NonNull SnapshotToMap<T> snapshotToMap) {
        this.dataSnapshot = dataSnapshot;
        this.snapshotToMap = snapshotToMap;
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
            map = snapshotToMap.from(dataSnapshot);
        return map;
    }

}
