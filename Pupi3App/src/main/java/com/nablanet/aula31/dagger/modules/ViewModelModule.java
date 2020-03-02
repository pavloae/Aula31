package com.nablanet.aula31.dagger.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.nablanet.aula31.core.viewmodel.Pupi3ViewModelFactory;

import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelModule {

    @Singleton
    @Provides
    ViewModelProvider.Factory viewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {
        return new Pupi3ViewModelFactory(providerMap);
    }

}
