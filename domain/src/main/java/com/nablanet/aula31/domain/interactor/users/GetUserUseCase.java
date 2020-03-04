package com.nablanet.aula31.domain.interactor.users;

import com.nablanet.aula31.domain.interactor.type.SingleUseCase;
import com.nablanet.aula31.domain.model.User;
import com.nablanet.aula31.domain.repository.MainRepository;

import io.reactivex.Single;

public class GetUserUseCase implements SingleUseCase<User> {

    MainRepository mainRepository;

    public GetUserUseCase(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @Override
    public Single<User> execute() {
        return mainRepository.getUser();
    }
}
