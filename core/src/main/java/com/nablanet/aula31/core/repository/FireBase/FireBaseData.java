package com.nablanet.aula31.core.repository.FireBase;

import androidx.annotation.NonNull;

import com.nablanet.aula31.domain.RequestUploadFile;
import com.nablanet.aula31.domain.ResultUploadFile;
import com.nablanet.aula31.domain.model.Phone;
import com.nablanet.aula31.domain.model.User;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface FireBaseData {

    Single<User> getUser();
    Single<Phone> getPhone();
    Completable saveUser(@NonNull User user);
    Completable savePhone(@NonNull Phone phone);
    Observable<ResultUploadFile> saveFile(RequestUploadFile requestUploadFile);

}
