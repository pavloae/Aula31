package com.nablanet.aula31.domain.interactor.phones;

import com.nablanet.aula31.domain.interactor.type.SingleUseCase;
import com.nablanet.aula31.domain.model.Phone;
import com.nablanet.aula31.domain.repository.MainRepository;

import io.reactivex.Single;

public class GetPhoneUseCase implements SingleUseCase<Phone> {

    MainRepository mainRepository;

    public GetPhoneUseCase(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @Override
    public Single<Phone> execute() {
        return mainRepository.getPhone();
    }
}
