package com.nablanet.aula31.dagger.modules;

import com.nablanet.aula31.domain.interactor.phones.GetPhoneUseCase;
import com.nablanet.aula31.domain.interactor.phones.SavePhoneUseCase;
import com.nablanet.aula31.domain.interactor.users.GetUserUseCase;
import com.nablanet.aula31.domain.interactor.users.SaveUserUseCase;
import com.nablanet.aula31.domain.repository.MainRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DomainModule {

    @Provides
    @Singleton
    GetUserUseCase provideGetUser(MainRepository mainRepository) {
        return new GetUserUseCase(mainRepository);
    }

    @Provides
    @Singleton
    GetPhoneUseCase provideGetPhone(MainRepository mainRepository) {
        return new GetPhoneUseCase(mainRepository);
    }

    @Provides
    @Singleton
    SaveUserUseCase provideSaveUser(MainRepository mainRepository) {
        return new SaveUserUseCase(mainRepository);
    }

    @Provides
    @Singleton
    SavePhoneUseCase provideSavePhone(MainRepository mainRepository) {
        return new SavePhoneUseCase(mainRepository);
    }

}
