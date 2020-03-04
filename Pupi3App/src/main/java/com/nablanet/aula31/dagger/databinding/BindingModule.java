package com.nablanet.aula31.dagger.databinding;

import com.nablanet.aula31.ImageLoaderGlide;
import com.nablanet.aula31.core.ImageLoader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BindingModule {

    @Provides
    @DataBinding
    ImageBindingAdapter provideImageBindingAdapter(ImageLoader imageLoader) {
        return new ImageBindingAdapter(imageLoader);
    }

    @Provides
    @DataBinding
    ImageLoader provideImageLoader(){
        return new ImageLoaderGlide();
    }

}
