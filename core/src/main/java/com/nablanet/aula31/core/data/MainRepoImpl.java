package com.nablanet.aula31.core.data;

import com.nablanet.aula31.domain.model.User;
import com.nablanet.aula31.domain.repository.MainRepository;

import io.reactivex.Single;

public class MainRepoImpl implements MainRepository {

    @Override
    public Single<User> getUser() {
        return null;
    }
}
