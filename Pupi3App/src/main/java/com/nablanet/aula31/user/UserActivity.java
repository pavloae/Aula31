package com.nablanet.aula31.user;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nablanet.aula31.R;
import com.nablanet.aula31.repo.DataResult;
import com.nablanet.aula31.repo.entity.Phone;
import com.nablanet.aula31.repo.entity.User;
import com.nablanet.aula31.user.viewmodel.UserViewModel;

public class UserActivity extends AppCompatActivity {

    ImageView userImage;
    EditText fieldLastname, fieldName, fieldComment;
    TextView phoneNumber;
    CheckBox shareNumber;

    User user;
    Phone phone;

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

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userViewModel.getOwnUserLiveData().observe(this, new Observer<DataResult<User>>() {
            @Override
            public void onChanged(@Nullable DataResult<User> userDataResult) {
                if (userDataResult != null && userDataResult.getDatabaseError() != null)
                    Toast.makeText(
                            UserActivity.this,
                            userDataResult.getDatabaseError().getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                else if (userDataResult == null || userDataResult.getValue() == null) {
                    user = null;
                    fieldLastname.setText(null);
                    fieldName.setText(null);
                    fieldComment.setText(null);
                } else {
                    user = userDataResult.getValue();
                    fieldLastname.setText(user.getLastname());
                    fieldName.setText(user.getNames());
                    fieldComment.setText(user.getComment());
                }
            }
        });

        userViewModel.getOwnPhoneLiveData().observe(this, new Observer<DataResult<Phone>>() {
            @Override
            public void onChanged(@Nullable DataResult<Phone> phoneDataResult) {
                if (phoneDataResult != null && phoneDataResult.getDatabaseError() != null)
                    Toast.makeText(
                            UserActivity.this,
                            phoneDataResult.getDatabaseError().getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                else if (phoneDataResult == null)
                    phone = null;
                else {
                    phone = phoneDataResult.getValue();
                    if (phone != null && phone.getShare() != null) {
                        shareNumber.setChecked(phone.getShare());
                        phoneNumber.setText(phone.getKey());
                    }
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
        user.setLastname(fieldLastname.getText().toString());
        user.setNames(fieldName.getText().toString());
        user.setComment(fieldComment.getText().toString());

        if (phone == null)
            phone = new Phone();
        phone.setShare(shareNumber.isChecked());

        userViewModel.update(user, phone);

    }

}
