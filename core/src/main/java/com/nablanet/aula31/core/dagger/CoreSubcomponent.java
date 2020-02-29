package com.nablanet.aula31.core.dagger;
import com.nablanet.aula31.core.Pupi3;
import com.nablanet.aula31.core.dagger.annotations.CoreScope;
import com.nablanet.aula31.core.dagger.modules.CoreModule;
import com.nablanet.aula31.core.dagger.modules.ViewModelModule;

import dagger.Subcomponent;

@CoreScope
@Subcomponent(modules = {CoreModule.class, ViewModelModule.class})
public interface CoreSubcomponent {

    Pupi3 getPupi3();

    @Subcomponent.Builder
    interface Builder {
        Builder requestModule(CoreModule module);
        CoreSubcomponent build();
    }

}
