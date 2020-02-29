package com.nablanet.aula31.core.repository.FireBase;

import com.nablanet.aula31.domain.model.Phone;
import com.nablanet.aula31.domain.model.User;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface FireBaseData {

    Single<User> getUser();
    Single<Phone> getPhone();
    Completable saveUser(User user);
    Completable savePhone(Phone phone);

}
