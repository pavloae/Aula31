package com.nablanet.aula31.dagger.modules;

import com.google.firebase.database.FirebaseDatabase;
import com.nablanet.aula31.core.repository.MainRepoImpl;
import com.nablanet.aula31.data.FireBaseDataImpl;
import com.nablanet.aula31.domain.repository.MainRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    @Provides
    @Singleton
    FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @Singleton
    MainRepository provideMainRepository(FireBaseDataImpl fireBaseData) {
        return new MainRepoImpl(fireBaseData);
    }
}
