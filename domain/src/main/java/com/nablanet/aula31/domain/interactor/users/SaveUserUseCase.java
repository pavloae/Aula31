package com.nablanet.aula31.domain.interactor.users;

import com.nablanet.aula31.domain.interactor.type.CompletableUseCase;
import com.nablanet.aula31.domain.model.Phone;
import com.nablanet.aula31.domain.model.User;
import com.nablanet.aula31.domain.repository.MainRepository;

import io.reactivex.Completable;

public class SaveUserUseCase implements CompletableUseCase<User> {

    MainRepository mainRepository;

    public SaveUserUseCase(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @Override
    public Completable execute(User user) {
        return mainRepository.saveUser(user);
    }

}
