package com.nablanet.aula31.core.dagger;
import com.nablanet.aula31.core.Pupi3;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = CoreModule.class)
public interface CoreComponent {

    Pupi3 getPupi3();

}
