package com.nablanet.aula31.core;

import android.content.Context;

import com.nablanet.aula31.core.dagger.CoreSubcomponent;

public class CoreInjectHelper {

    public static CoreSubcomponent provideCoreComponent(Context applicationContext) {

        if (applicationContext instanceof CoreComponentProvider)
            return ((CoreComponentProvider) applicationContext).provideCoreComponent();

        throw new IllegalStateException("La aplicación debe implementar CoreComponentProvider");

    }

}
