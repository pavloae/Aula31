package com.nablanet.aula31.export;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nablanet.aula31.R;
import com.nablanet.aula31.utils.Util;
import com.nablanet.documents.utils.FileManager;

import java.io.File;
import java.io.IOException;

public class ExportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Util.isExternalStorageWritable()) return;

                String templateName = "tracking_model.ods";
                File target = new File(getExternalCacheDir(), "tracking_cache.ods");

                try {
                    FileManager.createFile(getAssets().open(templateName), target);
                    //ZipManager.unzip(target);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }



}
