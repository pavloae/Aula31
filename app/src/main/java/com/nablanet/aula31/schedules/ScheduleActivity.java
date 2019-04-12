package com.nablanet.aula31.schedules;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.nablanet.aula31.R;
import com.nablanet.aula31.schedules.ui.schedule.ScheduleListFragment;

public class ScheduleActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        final Toolbar toolbar = findViewById(R.id.toolbar_schedules);
        toolbar.setTitle(R.string.title_course_activity);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ScheduleListFragment.newInstance())
                    .commitNow();
        }
    }
}
