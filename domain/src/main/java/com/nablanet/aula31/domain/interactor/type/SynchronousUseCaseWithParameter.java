package com.nablanet.aula31.domain.interactor.type;

public interface SynchronousUseCaseWithParameter<P, R> {

    R execute(P parameter);

}
