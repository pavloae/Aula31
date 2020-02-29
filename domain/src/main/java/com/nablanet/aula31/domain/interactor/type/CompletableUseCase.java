package com.nablanet.aula31.domain.interactor.type;

import io.reactivex.Completable;

public interface CompletableUseCase<P> {

    Completable execute(P value);

}
