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

import com.nablanet.aula31.R;
import com.nablanet.aula31.courses.entity.CourseProfileExt;
import com.nablanet.aula31.repo.Response;

public class CourseActivity extends AppCompatActivity {

    CourseViewModel courseViewModel;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        toolbar = findViewById(R.id.toolbar_courses);
        toolbar.setTitle(R.string.title_course_activity);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, new CoursesListFragment()).commit();

        setCourseViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_search:
                break;
            case R.id.btn_new:
                courseViewModel.loadCourseProfile(null);
                break;
                default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCourseViewModel() {
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);

        courseViewModel.getSelectedCourseProfile().observe(this, new Observer<CourseProfileExt>() {
            @Override
            public void onChanged(@Nullable CourseProfileExt courseProfileExt) {
                if (courseProfileExt == null) onBackPressed();
                else addFragment();
            }
        });
        courseViewModel.getResponseLiveData().observe(this, new Observer<Response>() {
            @Override
            public void onChanged(@Nullable Response response) {
                if (response == null) return;

                if (response.isSuccess())
                    switch (response.getCode())  {
                        case CourseViewModel.COURSE_SAVED:
                        case CourseViewModel.COURSE_UPDATED:
                            onBackPressed();
                            break;
                    }
                else
                    Snackbar.make(toolbar, response.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
    }

    private void addFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, new CourseProfileFragment())
                .addToBackStack(null)
                .commit();
    }

}
