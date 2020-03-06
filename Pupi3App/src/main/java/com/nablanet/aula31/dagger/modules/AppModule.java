package com.nablanet.aula31.dagger.modules;

import com.nablanet.aula31.ImageLoaderGlide;
import com.nablanet.aula31.core.ImageConverter;
import com.nablanet.aula31.core.ImageLoader;
import com.nablanet.aula31.core.Pupi3;
import com.nablanet.aula31.core.dagger.CoreSubcomponent;
import com.nablanet.aula31.databinding.BindingSubcomponent;
import com.nablanet.aula31.utils.ImageConverterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = {CoreSubcomponent.class, BindingSubcomponent.class})
public class AppModule {

    @Provides
    @Singleton
    Pupi3 providePupi3(){
        return new Pupi3(5);
    }

    @Provides
    @Singleton
    ImageLoader provideImageLoader(){
        return new ImageLoaderGlide();
    }

    @Provides
    @Singleton
    ImageConverter provideImageConverter() {
        return new ImageConverterImpl();
    }

}
