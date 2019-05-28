package com.nablanet.aula31.repo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class DataResult {

    private DataSnapshot dataSnapshot;
    private DatabaseError databaseError;

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
}
