package com.nablanet.aula31.core.dagger.modules;

import androidx.lifecycle.ViewModel;

import com.nablanet.aula31.core.dagger.annotations.CoreScope;
import com.nablanet.aula31.core.dagger.annotations.ViewModelKey;
import com.nablanet.aula31.core.viewmodel.UserViewModel;
import com.nablanet.aula31.domain.interactor.phones.GetPhone;
import com.nablanet.aula31.domain.interactor.phones.SavePhone;
import com.nablanet.aula31.domain.interactor.users.GetUser;
import com.nablanet.aula31.domain.interactor.users.SaveUser;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    @CoreScope
    ViewModel provideMainViewModel(GetUser getUser, GetPhone getPhone, SaveUser saveUser, SavePhone savePhone) {
        return new UserViewModel(getUser, getPhone, saveUser, savePhone);
    }

}
