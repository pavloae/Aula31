package com.nablanet.aula31.dagger.modules;

import com.nablanet.aula31.domain.interactor.phones.GetPhone;
import com.nablanet.aula31.domain.interactor.phones.SavePhone;
import com.nablanet.aula31.domain.interactor.users.GetUser;
import com.nablanet.aula31.domain.interactor.users.SaveUser;
import com.nablanet.aula31.domain.repository.MainRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DomainModule {

    @Provides
    @Singleton
    GetUser provideGetUser(MainRepository mainRepository) {
        return new GetUser(mainRepository);
    }

    @Provides
    @Singleton
    GetPhone provideGetPhone(MainRepository mainRepository) {
        return new GetPhone(mainRepository);
    }

    @Provides
    @Singleton
    SaveUser provideSaveUser(MainRepository mainRepository) {
        return new SaveUser(mainRepository);
    }

    @Provides
    @Singleton
    SavePhone provideSavePhone(MainRepository mainRepository) {
        return new SavePhone(mainRepository);
    }

}
