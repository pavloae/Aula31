package com.nablanet.aula31.dagger.databinding;

import androidx.databinding.DataBindingComponent;

import com.nablanet.aula31.dagger.AppComponent;

import dagger.Component;
import dagger.Subcomponent;

@DataBinding
@Component(dependencies = AppComponent.class, modules = BindingModule.class)
public interface BindingComponent extends DataBindingComponent {

/*    @Subcomponent.Builder
    interface Builder {
        BindingComponent build();
    }*/


}
