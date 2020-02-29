package com.nablanet.aula31.domain.interactor.type;

public interface CompletableUseCaseCallback<P> {

    interface Callback {
        void onSuccess();
        void onError(Throwable throwable);
    }

    void execute(P parameter, Callback callback);

}
