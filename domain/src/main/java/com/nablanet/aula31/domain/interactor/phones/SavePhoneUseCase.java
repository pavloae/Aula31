package com.nablanet.aula31.domain.interactor.phones;

import com.nablanet.aula31.domain.interactor.type.CompletableUseCase;
import com.nablanet.aula31.domain.model.Phone;
import com.nablanet.aula31.domain.repository.MainRepository;

import io.reactivex.Completable;

public class SavePhoneUseCase implements CompletableUseCase<Phone> {

    MainRepository mainRepository;

    public SavePhoneUseCase(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @Override
    public Completable execute(Phone phone) {
        return mainRepository.savePhone(phone);
    }

}
