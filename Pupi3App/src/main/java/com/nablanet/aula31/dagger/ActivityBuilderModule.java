package com.nablanet.aula31.dagger;

import com.nablanet.aula31.MainActivity;
import com.nablanet.aula31.login.PhoneAuthActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector
    abstract PhoneAuthActivity phoneAuthActivity();

}
