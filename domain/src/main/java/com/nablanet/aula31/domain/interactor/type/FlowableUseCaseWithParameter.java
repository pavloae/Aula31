package com.nablanet.aula31.domain.interactor.type;

import io.reactivex.Flowable;

public interface FlowableUseCaseWithParameter<P, R> {

    Flowable<R> execute(P parameter);

}
