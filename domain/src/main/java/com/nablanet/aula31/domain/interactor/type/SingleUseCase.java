package com.nablanet.aula31.domain.interactor.type;

import io.reactivex.Single;

public interface SingleUseCase<T> {

    Single<T> execute();

}
