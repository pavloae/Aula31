package com.nablanet.aula31.courses;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.nablanet.aula31.R;
import com.nablanet.aula31.courses.viewmodel.CourseViewModel;

public class CourseActivity extends AppCompatActivity {

    CourseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        Toolbar toolbar = findViewById(R.id.toolbar_courses);
        toolbar.setTitle(R.string.title_course_activity);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, new CoursesListFragment()).commit();

        viewModel = ViewModelProviders.of(this).get(CourseViewModel.class);

        viewModel.getCourseId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, new CourseProfileFragment())
                        .addToBackStack(null)
                        .commit();
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

        if (item.getItemId() == R.id.btn_new)
            viewModel.setCourseId(null);

        return super.onOptionsItemSelected(item);

    }

}
