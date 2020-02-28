package com.nablanet.aula31.dagger;

import com.nablanet.aula31.MainApp;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AppModule.class,
                AndroidSupportInjectionModule.class,
                ActivityBuilderModule.class
        })
public interface AppComponent extends AndroidInjector<MainApp> {

    void inject(MainApp mainApp);

}
