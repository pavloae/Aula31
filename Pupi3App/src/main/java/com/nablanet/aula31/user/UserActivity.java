package com.nablanet.aula31.user;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nablanet.aula31.R;
import com.nablanet.aula31.core.viewmodel.Response;
import com.nablanet.aula31.core.viewmodel.UserViewModel;
import com.nablanet.aula31.domain.model.Phone;
import com.nablanet.aula31.domain.model.User;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class UserActivity extends DaggerAppCompatActivity {

    ImageView userImage;
    EditText fieldLastname, fieldName, fieldComment;
    TextView phoneNumber;
    CheckBox shareNumber;

    User user;
    Phone phone;

    @Inject
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userImage = findViewById(R.id.pupil_image);
        fieldLastname = findViewById(R.id.lastname_et);
        fieldName = findViewById(R.id.name_et);
        fieldComment = findViewById(R.id.comment_et);
        phoneNumber = findViewById(R.id.phone_tv);
        shareNumber = findViewById(R.id.share_box);

        loadUser();

    }

    private void loadUser(){

        userViewModel.getOwnUserLiveData().observe(this, new Observer<Response<User>>() {
            @Override
            public void onChanged(Response<User> userResponse) {
                if (userResponse.success) {
                    user = userResponse.getValue();
                    fieldLastname.setText(user.lastname);
                    fieldName.setText(user.name);
                    fieldComment.setText(user.comment);
                } else {
                    user = null;
                    fieldLastname.setText(null);
                    fieldName.setText(null);
                    fieldComment.setText(null);
                    Toast.makeText(
                            UserActivity.this,
                            userResponse.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        userViewModel.getOwnPhoneLiveData().observe(this, new Observer<Response<Phone>>() {
            @Override
            public void onChanged(Response<Phone> phoneResponse) {
                if (phoneResponse.success) {
                    phone = phoneResponse.getValue();
                    shareNumber.setChecked(phone.share);
                    phoneNumber.setText(phone.key);
                } else {
                    phone = null;
                    Toast.makeText(
                            UserActivity.this,
                            phoneResponse.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
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
