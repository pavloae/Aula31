package com.nablanet.aula31.domain.interactor.type;

import io.reactivex.Flowable;

public interface FlowableUseCase<T> {

    Flowable<T> execute();

}
