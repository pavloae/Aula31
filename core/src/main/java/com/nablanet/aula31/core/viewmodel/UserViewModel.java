package com.nablanet.aula31.core.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nablanet.aula31.domain.interactor.phones.GetPhone;
import com.nablanet.aula31.domain.interactor.phones.SavePhone;
import com.nablanet.aula31.domain.interactor.users.GetUser;
import com.nablanet.aula31.domain.interactor.users.SaveUser;
import com.nablanet.aula31.domain.model.User;
import com.nablanet.aula31.domain.model.Phone;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserViewModel extends ViewModel {

    GetUser getUser;
    GetPhone getPhone;
    SaveUser saveUser;
    SavePhone savePhone;

    private MutableLiveData<Response<User>> userLiveData;
    private MutableLiveData<Response<Phone>> phoneLiveData;

    @Inject
    public UserViewModel(GetUser getUser, GetPhone getPhone, SaveUser saveUser, SavePhone savePhone) {
        this.getUser = getUser;
        this.getPhone = getPhone;
        this.saveUser = saveUser;
        this.savePhone = savePhone;
    }

    @NonNull
    public LiveData<Response<User>> getOwnUserLiveData() {
        if (userLiveData == null)
            userLiveData = new MutableLiveData<>();
        getUser.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onSuccess(User user) {
                        userLiveData.postValue(new Response<>(user, true, null));
                    }
                    @Override
                    public void onError(Throwable e) {
                        userLiveData.postValue(new Response<User>(true, e.toString()));
                    }
                });
        return userLiveData;
    }

    @NonNull
    public LiveData<Response<Phone>> getOwnPhoneLiveData() {
        if (phoneLiveData == null)
            phoneLiveData = new MutableLiveData<>();
        getPhone.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Phone>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Phone phone) {
                        phoneLiveData.postValue(new Response<>(phone, true, null));

                    }

                    @Override
                    public void onError(Throwable e) {
                        phoneLiveData.postValue(new Response<Phone>(false, e.toString()));
                    }
                });
        return phoneLiveData;
    }

    public void update(@Nullable User user, @Nullable Phone phone) {

        saveUser.execute(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR", e.getMessage());
                    }
                });

        savePhone.execute(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR", e.getMessage());
                    }
                });

    }

}
