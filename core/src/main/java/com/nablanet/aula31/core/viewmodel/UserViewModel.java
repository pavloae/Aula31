package com.nablanet.aula31.core.viewmodel;

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

    private MutableLiveData<User> user;
    public MutableLiveData<String> profileImageUrl = new MutableLiveData<>();
    private MutableLiveData<Phone> phone;
    private MutableLiveData<Response> response;

    @Inject
    public UserViewModel(
            GetUser getUser, GetPhone getPhone, SaveUser saveUser, SavePhone savePhone
    ) {
        this.getUser = getUser;
        this.getPhone = getPhone;
        this.saveUser = saveUser;
        this.savePhone = savePhone;
    }

    @NonNull
    public LiveData<User> getUser() {
        if (user == null)
            user = new MutableLiveData<>();
        getUser.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            UserViewModel.this.user.postValue(user);
                            UserViewModel.this.profileImageUrl.postValue(user.url_image);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                });
        return user;
    }

    @NonNull
    public LiveData<Phone> getPhone() {
        if (phone == null)
            phone = new MutableLiveData<>();
        getPhone.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Phone>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Phone phone) {
                        if (phone != null)
                            UserViewModel.this.phone.postValue(phone);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
        return phone;
    }

    @NonNull
    public MutableLiveData<Response> getResponse() {
        if (response == null)
            response = new MutableLiveData<>();
        return response;
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
                        getResponse().postValue(new Response(false, e.toString()));
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
                        getResponse().postValue(new Response(false, e.toString()));
                    }
                });

    }

}
