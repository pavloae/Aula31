package com.nablanet.aula31.domain.repository;

import com.nablanet.aula31.domain.model.User;

import io.reactivex.Single;

public interface MainRepository {

    Single<User> getUser();

}
