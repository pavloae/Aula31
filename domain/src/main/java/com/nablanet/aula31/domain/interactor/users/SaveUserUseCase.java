package com.nablanet.aula31.domain.interactor.users;

import com.nablanet.aula31.domain.RequestUploadFile;
import com.nablanet.aula31.domain.ResultUploadFile;
import com.nablanet.aula31.domain.interactor.type.CompletableUseCase;
import com.nablanet.aula31.domain.model.User;
import com.nablanet.aula31.domain.repository.MainRepository;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class SaveUserUseCase implements CompletableUseCase<User> {

    MainRepository mainRepository;

    public SaveUserUseCase(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @Override
    public Completable execute(User user) {
        return mainRepository.saveUser(user);
    }

    public Observable<ResultUploadFile> saveFile(RequestUploadFile requestUploadFile) {
        return mainRepository.saveFile(requestUploadFile);
    }

}
