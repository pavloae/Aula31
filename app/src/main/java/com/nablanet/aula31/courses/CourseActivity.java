package com.nablanet.aula31.courses;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseError;
import com.nablanet.aula31.R;

public class CourseActivity extends AppCompatActivity {

    CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        final Toolbar toolbar = findViewById(R.id.toolbar_courses);
        toolbar.setTitle(R.string.title_course_activity);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, new CoursesListFragment()).commit();

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getDatabaseError().observe(this, new Observer<DatabaseError>() {
            @Override
            public void onChanged(@Nullable DatabaseError databaseError) {
                if (databaseError != null)
                    Snackbar.make(toolbar, databaseError.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
        courseViewModel.getSelectedCourseProfile().observe(this, new Observer<Course.Profile>() {
            @Override
            public void onChanged(@Nullable Course.Profile courseProfile) {
                if (courseProfile == null) onBackPressed();
                else addFragment();
            }
        });
        courseViewModel.getSuccessMutableLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == null) return;
                switch (integer)  {
                    case CourseViewModel.COURSE_SAVED:
                    case CourseViewModel.COURSE_UPDATED:
                        onBackPressed();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_new:
                courseViewModel.loadCourseProfile(null);
                break;
                // TODO: future options
        }
        return super.onOptionsItemSelected(item);
    }

    private void addFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, new CourseProfileFragment())
                .addToBackStack(null)
                .commit();
    }

}
