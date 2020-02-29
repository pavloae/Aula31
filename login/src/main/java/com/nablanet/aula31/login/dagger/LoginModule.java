package com.nablanet.aula31.login.dagger;

import com.nablanet.aula31.core.Pupi3;
import com.nablanet.aula31.core.dagger.annotations.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    @ActivityScope
    Pupi3 providePupi3() {
        return new Pupi3(5);
    }

}
