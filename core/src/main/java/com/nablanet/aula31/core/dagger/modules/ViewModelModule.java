package com.nablanet.aula31.core.dagger.modules;

import androidx.lifecycle.ViewModel;

import com.nablanet.aula31.core.ImageConverter;
import com.nablanet.aula31.core.dagger.annotations.CoreScope;
import com.nablanet.aula31.core.dagger.annotations.ViewModelKey;
import com.nablanet.aula31.core.viewmodel.UserViewModel;
import com.nablanet.aula31.domain.interactor.phones.GetPhoneUseCase;
import com.nablanet.aula31.domain.interactor.phones.SavePhoneUseCase;
import com.nablanet.aula31.domain.interactor.users.GetUserUseCase;
import com.nablanet.aula31.domain.interactor.users.SaveUserUseCase;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    @CoreScope
    ViewModel provideMainViewModel(
            GetUserUseCase getUserUseCase, GetPhoneUseCase getPhoneUseCase,
            SaveUserUseCase saveUserUseCase, SavePhoneUseCase savePhoneUseCase,
            ImageConverter imageConverter
    ) {
        return new UserViewModel(
                getUserUseCase, getPhoneUseCase, saveUserUseCase, savePhoneUseCase, imageConverter
        );
    }

}
