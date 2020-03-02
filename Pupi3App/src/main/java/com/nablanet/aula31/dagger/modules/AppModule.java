package com.nablanet.aula31.dagger.modules;

import com.nablanet.aula31.core.Pupi3;
import com.nablanet.aula31.core.dagger.CoreSubcomponent;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = {CoreSubcomponent.class})
public class AppModule {

    @Provides
    @Singleton
    Pupi3 providePupi3(){
        return new Pupi3(5);
    }

}
