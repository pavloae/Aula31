package com.nablanet.aula31.domain.interactor.type;

import io.reactivex.Single;

public interface SingleUseCaseWithParameter<P, R> {

    Single<R> execute(P parameter);

}
