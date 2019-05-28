package com.nablanet.aula31.schedules;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nablanet.aula31.R;
import com.nablanet.aula31.courses.entity.CourseExt;
import com.nablanet.aula31.views.DayScheduleView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleCalendarActivity extends AppCompatActivity implements View.OnTouchListener {

    SparseArray<DayScheduleView> dayScheduleViews;
    List<CourseExt> courses;
    String userId;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar_schedule);
        toolbar.setSubtitle(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);

        dayScheduleViews = new SparseArray<>();
        dayScheduleViews.put(Calendar.MONDAY, (DayScheduleView) findViewById(R.id.schedule_monday));
        dayScheduleViews.put(Calendar.TUESDAY, (DayScheduleView) findViewById(R.id.schedule_tuesday));
        dayScheduleViews.put(Calendar.WEDNESDAY, (DayScheduleView) findViewById(R.id.schedule_wednesday));
        dayScheduleViews.put(Calendar.THURSDAY, (DayScheduleView) findViewById(R.id.schedule_thursday));
        dayScheduleViews.put(Calendar.FRIDAY, (DayScheduleView) findViewById(R.id.schedule_friday));

        dayScheduleViews.get(Calendar.MONDAY).setOnTouchListener(this);
        dayScheduleViews.get(Calendar.TUESDAY).setOnTouchListener(this);
        dayScheduleViews.get(Calendar.WEDNESDAY).setOnTouchListener(this);
        dayScheduleViews.get(Calendar.THURSDAY).setOnTouchListener(this);
        dayScheduleViews.get(Calendar.FRIDAY).setOnTouchListener(this);

        userId = FirebaseAuth.getInstance().getUid();

        pullCourses();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        return true;
    }

    private void pullCourses(){
        courses = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference("courses")
                .orderByValue().equalTo(userId).limitToFirst(100);



        progressBar.setVisibility(View.VISIBLE);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                List<CourseExt> courses = new ArrayList<>();
                CourseExt course;
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    course = child.getValue(CourseExt.class);
                    if (course != null && course.profile.year == Calendar.getInstance().get(Calendar.YEAR)) {
                        courses.add(course);
                    }
                }
                DayScheduleView.setCourses(courses);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(progressBar, databaseError.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        DayScheduleView.setMarginTime(8 * 60, 24 * 60);
    }

}
