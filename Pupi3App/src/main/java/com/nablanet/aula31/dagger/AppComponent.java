package com.nablanet.aula31.dagger;

import com.nablanet.aula31.MainApp;
import com.nablanet.aula31.dagger.modules.DataModule;
import com.nablanet.aula31.dagger.modules.ActivityBuilderModule;
import com.nablanet.aula31.dagger.modules.AppModule;
import com.nablanet.aula31.dagger.modules.DomainModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AppModule.class,
                DataModule.class,
                DomainModule.class,
                AndroidSupportInjectionModule.class,
                ActivityBuilderModule.class
        })
public interface AppComponent extends AndroidInjector<MainApp> {

    void inject(MainApp mainApp);

}
