package com.nablanet.aula31;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
