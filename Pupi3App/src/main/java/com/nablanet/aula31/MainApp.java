package com.nablanet.aula31;

import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.FirebaseDatabase;
import com.nablanet.aula31.dagger.AppComponent;
import com.nablanet.aula31.dagger.DaggerAppComponent;
import com.nablanet.aula31.dagger.databinding.BindingComponent;
import com.nablanet.aula31.dagger.databinding.DaggerBindingComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class MainApp extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().build();
        BindingComponent bindingComponent = DaggerBindingComponent.builder().appComponent(appComponent).build();
        DataBindingUtil.setDefaultComponent(bindingComponent);
        return DaggerAppComponent.builder().build();
    }
}
