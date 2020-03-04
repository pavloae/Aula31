package com.nablanet.aula31.databinding;

import com.nablanet.aula31.core.ImageLoader;

import dagger.Module;
import dagger.Provides;

@Module
public class BindingModule {

    @Provides
    @DataBinding
    ImageBindingAdapter provideImageBindingAdapter(ImageLoader imageLoader) {
        return new ImageBindingAdapter(imageLoader);
    }

}
