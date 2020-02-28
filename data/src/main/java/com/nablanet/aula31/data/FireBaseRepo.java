package com.nablanet.aula31.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nablanet.aula31.core.data.FireBaseData;
import com.nablanet.aula31.core.data.entity.User;

import javax.inject.Inject;

public class FireBaseRepo implements FireBaseData {

    public final FirebaseDatabase dbInstance;

    @Inject
    public FireBaseRepo(FirebaseDatabase dbInstance) {
        this.dbInstance = dbInstance;
    }

    @Override
    public User getUser() {
        return null;
    }

    public String getUid(){
        return FirebaseAuth.getInstance().getUid();
    }
}
