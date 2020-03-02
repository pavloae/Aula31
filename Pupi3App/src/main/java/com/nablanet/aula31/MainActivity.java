package com.nablanet.aula31;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.nablanet.aula31.core.Pupi3;
import com.nablanet.aula31.core.viewmodel.UserViewModel;
import com.nablanet.aula31.courses.CourseActivity;
import com.nablanet.aula31.export.ExportActivity;
import com.nablanet.aula31.login.PhoneAuthActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    private static final int SIGNUP = 0;

    @Inject
    Pupi3 pupi3;

    @Inject
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_main));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            startActivityForResult(new Intent(this, PhoneAuthActivity.class), SIGNUP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btn_help:
            case R.id.btn_about:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onSelect(View v) {
        switch (v.getId()) {
            case R.id.user_button:
                startActivity(new Intent(this, UserActivity.class));
                break;
            case R.id.courses_button:
                startActivity(new Intent(this, CourseActivity.class));
                break;
            case R.id.memberships_button:
                startActivity(new Intent(this, ExportActivity.class));
                break;
            case R.id.calendar_button:
                startActivity(new Intent(this, PhoneAuthActivity.class));
                Toast.makeText(this, "En producción...", Toast.LENGTH_SHORT).show();
        }
    }

}
