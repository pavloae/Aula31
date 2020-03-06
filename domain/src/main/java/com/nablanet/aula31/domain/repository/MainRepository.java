package com.nablanet.aula31.domain.repository;

import com.nablanet.aula31.domain.RequestUploadFile;
import com.nablanet.aula31.domain.ResultUploadFile;
import com.nablanet.aula31.domain.model.Phone;
import com.nablanet.aula31.domain.model.User;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface MainRepository {

    Single<User> getUser();

    Completable saveUser(User user);

    Single<Phone> getPhone();

    Completable savePhone(Phone phone);

    Observable<ResultUploadFile> saveFile(RequestUploadFile requestUploadFile);

}
