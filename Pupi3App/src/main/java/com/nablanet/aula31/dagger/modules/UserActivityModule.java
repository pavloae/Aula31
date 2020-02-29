package com.nablanet.aula31.dagger.modules;

import com.nablanet.aula31.core.dagger.annotations.ActivityScope;
import com.nablanet.aula31.core.viewmodel.UserViewModel;
import com.nablanet.aula31.user.UserActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class UserActivityModule {

    UserActivity activity;

    @Provides
    @ActivityScope
    UserViewModel provideUserViewModel(){
        return null;
    }

}
