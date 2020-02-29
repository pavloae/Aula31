package com.nablanet.aula31.login.dagger;

import com.nablanet.aula31.core.dagger.annotations.ActivityScope;
import com.nablanet.aula31.login.PhoneAuthActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@ActivityScope
@Subcomponent(modules = LoginModule.class)
public interface LoginComponent extends AndroidInjector<PhoneAuthActivity> {

    @Subcomponent.Factory
    abstract class Factory implements AndroidInjector.Factory<PhoneAuthActivity>{}

}
