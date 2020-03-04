package com.nablanet.aula31.core.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nablanet.aula31.domain.interactor.phones.GetPhoneUseCase;
import com.nablanet.aula31.domain.interactor.phones.SavePhoneUseCase;
import com.nablanet.aula31.domain.interactor.users.GetUserUseCase;
import com.nablanet.aula31.domain.interactor.users.SaveUserUseCase;
import com.nablanet.aula31.domain.model.User;
import com.nablanet.aula31.domain.model.Phone;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserViewModel extends ViewModel {

    GetUserUseCase getUserUseCase;
    GetPhoneUseCase getPhoneUseCase;
    SaveUserUseCase saveUserUseCase;
    SavePhoneUseCase savePhoneUseCase;

    private MutableLiveData<User> user = new MutableLiveData<>();
    public MutableLiveData<String> lastname = new MutableLiveData<>();
    public MutableLiveData<String> names = new MutableLiveData<>();
    public MutableLiveData<String> comment = new MutableLiveData<>();
    public MutableLiveData<String> imageUrl = new MutableLiveData<>();

    private MutableLiveData<Phone> phone = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<Boolean> shared = new MutableLiveData<>();

    public MutableLiveData<Response> response = new MutableLiveData<>();

    @Inject
    public UserViewModel(
            GetUserUseCase getUserUseCase, GetPhoneUseCase getPhoneUseCase,
            SaveUserUseCase saveUserUseCase, SavePhoneUseCase savePhoneUseCase
    ) {
        this.getUserUseCase = getUserUseCase;
        this.getPhoneUseCase = getPhoneUseCase;
        this.saveUserUseCase = saveUserUseCase;
        this.savePhoneUseCase = savePhoneUseCase;
    }

    public void fetchUser() {
        getUserUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onSuccess(User user) {
                        UserViewModel.this.user.postValue(user);
                        if (user != null) {
                            lastname.setValue(user.lastname);
                            names.setValue(user.names);
                            comment.setValue(user.comment);
                            imageUrl.setValue(user.url_image);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        response.postValue(new Response(false, e.toString()));
                    }
                });
    }

    public void fetchPhone() {
        getPhoneUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Phone>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Phone phone) {
                        UserViewModel.this.phone.postValue(phone);
                        if (phone != null) {
                            phoneNumber.setValue(phone.key);
                            shared.setValue(phone.share);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        response.postValue(new Response(false, e.toString()));
                    }
                });
    }

    public void update() {

        User user = this.user.getValue();
        if (user != null)
            save(user);

        Phone phone = this.phone.getValue();
        if (phone != null)
            save(phone);

        fetchUser();
        fetchPhone();

    }

    private void save(@NonNull User user) {
        user.lastname = lastname.getValue();
        user.names = names.getValue();
        user.comment = comment.getValue();

        saveUserUseCase.execute(user)
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
                        response.postValue(new Response(false, e.toString()));
                    }
                });

    }

    private void save(@NonNull Phone phone) {

        phone.share = shared.getValue();

        savePhoneUseCase.execute(phone)
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
                        response.postValue(new Response(false, e.toString()));
                    }
                });
    }

}
