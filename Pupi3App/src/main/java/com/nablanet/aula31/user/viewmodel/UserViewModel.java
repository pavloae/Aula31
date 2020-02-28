package com.nablanet.aula31.user.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.nablanet.aula31.repo.DataResult;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.entity.Phone;
import com.nablanet.aula31.repo.entity.User;

public class UserViewModel extends ViewModel {

    FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    private MutableLiveData<Response> responseLive;

    private LiveData<DataResult<User>> userLiveData;
    private LiveData<DataResult<Phone>> phoneLiveData;

    @NonNull
    LiveData<Response> getResponse() {
        if (responseLive == null)
            responseLive = new MutableLiveData<>();
        return responseLive;
    }

    @NonNull
    public LiveData<DataResult<User>> getOwnUserLiveData() {
        if (userLiveData == null)
            userLiveData = fireBaseRepo.getOwnUser();
        return userLiveData;
    }

    @NonNull
    public LiveData<DataResult<Phone>> getOwnPhoneLiveData() {
        if (phoneLiveData == null)
            phoneLiveData = fireBaseRepo.getOwnPhone();
        return phoneLiveData;
    }

    public void update(@Nullable User user, @Nullable Phone phone) {
        fireBaseRepo.update(user, phone, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseLive.setValue(new Response(false, e.getMessage()));
            }
        });
    }

}
