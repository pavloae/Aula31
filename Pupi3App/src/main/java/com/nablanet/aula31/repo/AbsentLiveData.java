package com.nablanet.aula31.repo;

import androidx.lifecycle.LiveData;

public class AbsentLiveData<T> extends LiveData<T> {

    @Override
    protected void onActive() {
        super.onActive();
        postValue(null);
    }

}
