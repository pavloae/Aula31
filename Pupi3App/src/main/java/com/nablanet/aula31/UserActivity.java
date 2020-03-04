package com.nablanet.aula31;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nablanet.aula31.core.viewmodel.Response;
import com.nablanet.aula31.core.viewmodel.UserViewModel;
import com.nablanet.aula31.databinding.ActivityUserBinding;
import com.nablanet.aula31.domain.model.Phone;
import com.nablanet.aula31.domain.model.User;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class UserActivity extends DaggerAppCompatActivity {

    EditText fieldLastname, fieldName, fieldComment;
    CheckBox shareNumber;

    User user;
    Phone phone;

    @Inject
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserBinding activityUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        activityUserBinding.setLifecycleOwner(this);
        activityUserBinding.setViewmodel(userViewModel);

        Toolbar toolbar = findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void updateUser(View view){

        if (fieldLastname.length() == 0 || fieldLastname.length() >= 20){
            fieldLastname.setError("El Apellido debe tener entre 1 y 20 caracteres");
            return;
        }

        if (fieldName.length() == 0 || fieldName.length() >= 20){
            fieldName.setError("Los nombres deben deben ocupar entre 1 y 20 caracteres");
            return;
        }

        if (fieldComment.length() >= 50){
            fieldComment.setError("El comentario debe tener menos de 50 caracteres");
            return;
        }

        if (user == null)
            user = new User();
        user.lastname = fieldLastname.getText().toString();
        user.name = fieldName.getText().toString();
        user.comment = fieldComment.getText().toString();

        if (phone == null)
            phone = new Phone();
        phone.share = shareNumber.isChecked();

        userViewModel.update(user, phone);

    }

}
