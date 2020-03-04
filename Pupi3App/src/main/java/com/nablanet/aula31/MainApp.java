package com.nablanet.aula31;

import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.FirebaseDatabase;
import com.nablanet.aula31.dagger.AppComponent;
import com.nablanet.aula31.dagger.DaggerAppComponent;
import com.nablanet.aula31.databinding.BindingSubcomponent;

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
        AppComponent appComponent = DaggerAppComponent.create();
        BindingSubcomponent bindingSubcomponent = appComponent.getBindingComponentFactory().build();
        DataBindingUtil.setDefaultComponent(bindingSubcomponent);
        return appComponent;
    }
}
