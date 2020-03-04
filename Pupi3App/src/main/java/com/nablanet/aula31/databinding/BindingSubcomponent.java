package com.nablanet.aula31.databinding;

import androidx.databinding.DataBindingComponent;

import dagger.Subcomponent;

@DataBinding
@Subcomponent(modules = BindingModule.class)
public interface BindingSubcomponent extends DataBindingComponent {

    @Subcomponent.Factory
    interface Factory {
        BindingSubcomponent build();
    }

}
