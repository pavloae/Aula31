package com.nablanet.aula31.works;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.nablanet.aula31.R;
import com.nablanet.aula31.works.ui.work.WorkDetailFragment;

public class WorkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WorkDetailFragment.newInstance())
                    .commitNow();
        }
    }
}
