package com.nablanet.aula31.domain.interactor.users;

import com.nablanet.aula31.domain.interactor.type.SingleUseCase;
import com.nablanet.aula31.domain.model.User;
import com.nablanet.aula31.domain.repository.MainRepository;

import io.reactivex.Single;

public class GetUserWithParameter implements SingleUseCase<User> {

    public MainRepository mainRepository;

    @Override
    public Single<User> execute() {
        return mainRepository.getUser();
    }
}
