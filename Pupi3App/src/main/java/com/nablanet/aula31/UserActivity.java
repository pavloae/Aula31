package com.nablanet.aula31;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;
import com.nablanet.aula31.core.viewmodel.Response;
import com.nablanet.aula31.core.viewmodel.UserViewModel;
import com.nablanet.aula31.databinding.ActivityUserBinding;
import com.nablanet.aula31.domain.ResultUploadFile;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class UserActivity extends DaggerAppCompatActivity {

    @Inject
    UserViewModel userViewModel;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserBinding activityUserBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_user
        );
        activityUserBinding.setLifecycleOwner(this);
        activityUserBinding.setViewmodel(userViewModel);

        progressBar = findViewById(R.id.progress_circular);

        Toolbar toolbar = findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindViewModel();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            userViewModel.saveImageProfile(data.getData());
        }
    }

    private void bindViewModel() {

        userViewModel.update();

        userViewModel.progress.observe(this, new Observer<ResultUploadFile>() {
            @Override
            public void onChanged(ResultUploadFile resultUploadFile) {
                if (resultUploadFile == null) {
                    progressBar.setVisibility(ProgressBar.GONE);
                } else {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    progressBar.setProgress(resultUploadFile.getIntPercentProgress());
                }
            }
        });

        userViewModel.response.observe(this, new Observer<Response>() {
            @Override
            public void onChanged(Response response) {
                if (response.success)
                    Toast.makeText(
                            UserActivity.this, response.getMessage(), Toast.LENGTH_SHORT
                    ).show();
                else
                    Snackbar.make(findViewById(android.R.id.content), response.getMessage(),
                            Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    public void selectImage(View view) {
        // TODO: Hacer una activity como la gente para cargar la imagen
        Intent intent = new Intent();
        intent.setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent,"Seleccione una imagen..."), 0
        );
    }

}
