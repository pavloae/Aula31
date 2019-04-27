package com.nablanet.aula31;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.nablanet.aula31.courses.CourseActivity;
import com.nablanet.aula31.export.ExportActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_main));

        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);

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
                break;
            case R.id.btn_about:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;

        switch (v.getId()) {
            case R.id.button0: intent = new Intent(this, UserActivity.class); break;
            case R.id.button1: intent = new Intent(this, CourseActivity.class); break;
            case R.id.button2: intent = new Intent(this, ExportActivity.class); break;
            case R.id.button3:
                Toast.makeText(this, "En producción...", Toast.LENGTH_SHORT).show();
        }

        if (intent != null)
            startActivity(intent);

    }

    private void createODS() {

    }

}
