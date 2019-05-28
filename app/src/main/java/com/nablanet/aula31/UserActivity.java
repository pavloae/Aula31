package com.nablanet.aula31;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nablanet.aula31.pojos.Phone;
import com.nablanet.aula31.pojos.User;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    ImageView userImage;
    EditText fieldLastname, fieldName, fieldComment;
    TextView phoneNumber;
    CheckBox shareNumber;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    User user;
    Phone phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        userImage = findViewById(R.id.pupil_image);
        fieldLastname = findViewById(R.id.lastname_et);
        fieldName = findViewById(R.id.name_et);
        fieldComment = findViewById(R.id.comment_et);
        phoneNumber = findViewById(R.id.phone_tv);
        shareNumber = findViewById(R.id.share_box);

        loadUser();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });

        FirebaseAuth.getInstance().getAccessToken(true)
                .addOnCompleteListener(
                        new OnCompleteListener<GetTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                Log.d("USER", "Token: " + task.getResult().getToken());
                            }
                        }
                ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(userImage, e.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
        );

    }

    private void loadUser(){

        if (databaseReference == null || firebaseUser == null || firebaseUser.getPhoneNumber() == null)
            return;

        phoneNumber.setText(firebaseUser.getPhoneNumber());

        databaseReference
                .child("users")
                .child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        if (user != null){
                            fieldLastname.setText(user.lastname);
                            fieldName.setText(user.names);
                            fieldComment.setText(user.comment);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Snackbar.make(userImage, databaseError.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

        databaseReference
                .child("phones")
                .child(firebaseUser.getPhoneNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        phone = dataSnapshot.getValue(Phone.class);
                        if (phone != null)
                            shareNumber.setChecked(phone.share);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Snackbar.make(userImage, databaseError.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

    }

    private void updateUser(){

        if (fieldName.length() == 0 || fieldName.length() >= 20){
            fieldName.setError("El fullName debe tener entre 1 y 20 caracteres");
            return;
        }

        if (fieldComment.length() >= 50){
            fieldComment.setError("El comment debe tener menos de 50 caracteres");
            return;
        }

        if (user == null)
            user = new User().setUid(firebaseUser.getUid());
        user.lastname = fieldLastname.getText().toString();
        user.setName(fieldName.getText().toString()).setComment(fieldComment.getText().toString());

        if (phone == null)
            phone = new Phone().setUid(firebaseUser.getUid());
        phone.setShare(shareNumber.isChecked());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + firebaseUser.getUid(), user.toMap());
        childUpdates.put("/phones/" + firebaseUser.getPhoneNumber(), phone.toMap());

        databaseReference.updateChildren(childUpdates).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(userImage, e.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
        );

    }

}
