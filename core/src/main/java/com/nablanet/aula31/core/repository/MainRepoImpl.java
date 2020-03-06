package com.nablanet.aula31.core.repository;

import com.nablanet.aula31.core.repository.FireBase.FireBaseData;
import com.nablanet.aula31.domain.RequestUploadFile;
import com.nablanet.aula31.domain.ResultUploadFile;
import com.nablanet.aula31.domain.model.Phone;
import com.nablanet.aula31.domain.model.User;
import com.nablanet.aula31.domain.repository.MainRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class MainRepoImpl implements MainRepository {

    FireBaseData fireBaseData;

    @Inject
    public MainRepoImpl(FireBaseData fireBaseData) {
        this.fireBaseData = fireBaseData;
    }

    @Override
    public Single<User> getUser() {
        return fireBaseData.getUser();
    }

    @Override
    public Completable saveUser(User user) {
        return fireBaseData.saveUser(user);
    }

    @Override
    public Single<Phone> getPhone() {
        return fireBaseData.getPhone();
    }

    @Override
    public Completable savePhone(Phone phone) {
        return fireBaseData.savePhone(phone);
    }

    @Override
    public Observable<ResultUploadFile> saveFile(RequestUploadFile requestUploadFile) {
        return fireBaseData.saveFile(requestUploadFile);
    }


}
