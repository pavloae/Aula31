package com.nablanet.aula31.dagger;

import com.nablanet.aula31.core.Pupi3;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    Pupi3 providePupi3(){
        return new Pupi3(5);
    }

}
