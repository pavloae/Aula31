package com.nablanet.aula31.core.viewmodel;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nablanet.aula31.core.ImageConverter;
import com.nablanet.aula31.core.utils.UriConverter;
import com.nablanet.aula31.domain.RequestUploadFile;
import com.nablanet.aula31.domain.ResultUploadFile;
import com.nablanet.aula31.domain.interactor.phones.GetPhoneUseCase;
import com.nablanet.aula31.domain.interactor.phones.SavePhoneUseCase;
import com.nablanet.aula31.domain.interactor.users.GetUserUseCase;
import com.nablanet.aula31.domain.interactor.users.SaveUserUseCase;
import com.nablanet.aula31.domain.model.User;
import com.nablanet.aula31.domain.model.Phone;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserViewModel extends ViewModel {

    GetUserUseCase getUserUseCase;
    GetPhoneUseCase getPhoneUseCase;
    SaveUserUseCase saveUserUseCase;
    SavePhoneUseCase savePhoneUseCase;
    ImageConverter imageConverter;

    private MutableLiveData<User> user = new MutableLiveData<>();
    public MutableLiveData<String> lastname = new MutableLiveData<>();
    public MutableLiveData<String> names = new MutableLiveData<>();
    public MutableLiveData<String> comment = new MutableLiveData<>();
    public MutableLiveData<String> imageUrl = new MutableLiveData<>();

    private MutableLiveData<Phone> phone = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<Boolean> shared = new MutableLiveData<>();

    public MutableLiveData<Response> response = new MutableLiveData<>();
    public MutableLiveData<ResultUploadFile> progress = new MutableLiveData<>();

    @Inject
    public UserViewModel(
            GetUserUseCase getUserUseCase, GetPhoneUseCase getPhoneUseCase,
            SaveUserUseCase saveUserUseCase, SavePhoneUseCase savePhoneUseCase,
            ImageConverter imageConverter
    ) {
        this.getUserUseCase = getUserUseCase;
        this.getPhoneUseCase = getPhoneUseCase;
        this.saveUserUseCase = saveUserUseCase;
        this.savePhoneUseCase = savePhoneUseCase;
        this.imageConverter = imageConverter;
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

    public void saveImageProfile(@NonNull Uri uri) {

        String path = "profile"+ File.separator + uri.getLastPathSegment();

        URI jUri;
        try {
            jUri = UriConverter.getURI(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            response.setValue(new Response(false, e.getMessage()));
            return;
        }

        saveUserUseCase.saveFile(new RequestUploadFile(path, jUri))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResultUploadFile>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        progress.setValue(new ResultUploadFile(0, 0));
                    }

                    @Override
                    public void onNext(ResultUploadFile resultUploadFile) {
                        progress.setValue(resultUploadFile);
                        if (resultUploadFile.complete)
                            imageUrl.setValue(resultUploadFile.url.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        response.setValue(new Response(false, e.getMessage()));
                        progress.setValue(null);
                    }

                    @Override
                    public void onComplete() {
                        progress.setValue(null);
                        response.setValue(new Response(true, "Imagen cargada!"));
                    }
                });

    }

    private void save(@NonNull User user) {

        user.lastname = lastname.getValue();
        user.names = names.getValue();
        user.comment = comment.getValue();
        user.url_image = imageUrl.getValue();

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
