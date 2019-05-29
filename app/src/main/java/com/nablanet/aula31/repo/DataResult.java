package com.nablanet.aula31.repo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Map;

public class DataResult<T> {

    private DataSnapshot dataSnapshot;
    private DatabaseError databaseError;
    private Map map;

    public DataResult(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
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

    public void makeMapFrom(SnapshotToMap snapshotToMap) {
        map = snapshotToMap.from(dataSnapshot);
    }

    public Map getMap() {
        return map;
    }

}
