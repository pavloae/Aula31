package com.nablanet.aula31.login.dagger;

import com.nablanet.aula31.core.dagger.FeatureScope;
import com.nablanet.aula31.login.PhoneAuthActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@FeatureScope
@Subcomponent(modules = LoginModule.class)
public interface LoginComponent extends AndroidInjector<PhoneAuthActivity> {

    @Subcomponent.Factory
    abstract class Factory implements AndroidInjector.Factory<PhoneAuthActivity>{}

}
