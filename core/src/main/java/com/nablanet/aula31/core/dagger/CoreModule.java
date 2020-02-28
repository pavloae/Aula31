package com.nablanet.aula31.core.dagger;

import com.nablanet.aula31.core.Pupi3;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CoreModule {

    Pupi3 pupi3;

    @Provides
    @Singleton
    Pupi3 providePupi3(){
        if (pupi3 == null)
            pupi3 = new Pupi3();
        return pupi3;
    }

}
