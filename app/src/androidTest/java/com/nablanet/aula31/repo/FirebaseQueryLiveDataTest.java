package com.nablanet.aula31.repo;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nablanet.aula31.export.entity.CourseExt;
import com.nablanet.aula31.repo.entity.Course;
import com.nablanet.aula31.repo.query.EventRecord;
import com.nablanet.aula31.repo.query.ReadFuture;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RunWith(AndroidJUnit4.class)
public class FirebaseQueryLiveDataTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    public DatabaseReference testReference = FirebaseDatabase.getInstance().getReference("test");

    @Before
    public void setUp() throws Exception {

        if (Looper.myLooper() == null)
            Looper.prepare();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCourses() throws ExecutionException, InterruptedException {

        DatabaseReference dbref = testReference.child("courses").child("course1");

        FirebaseQueryLiveData<Course> firebaseQueryLiveData =  new FirebaseQueryLiveData<>(
                dbref, Course.class
        );
        firebaseQueryLiveData.observeForever(
                new Observer<DataResult<Course>>() {
                    @Override
                    public void onChanged(@Nullable DataResult<Course> courseDataResult) {
                        Assert.assertNotNull(courseDataResult);
                        Course course = courseDataResult.getValue();
                        Assert.assertNotNull(course);
                        Assert.assertNotNull(course.getMembers());
                        Assert.assertNotNull(course.getProfile());
                    }
                }
        );

        ReadFuture.untilNonNull(dbref).get();

    }

    @Test
    public void getCoursesExt() throws ExecutionException, InterruptedException {

        DatabaseReference dbref = testReference.child("courses").child("course1");

        FirebaseQueryLiveData<CourseExt> firebaseQueryLiveData =  new FirebaseQueryLiveData<>(
                dbref, CourseExt.class
        );
        firebaseQueryLiveData.observeForever(
                new Observer<DataResult<CourseExt>>() {
                    @Override
                    public void onChanged(@Nullable DataResult<CourseExt> courseDataResult) {
                        Assert.assertNotNull(courseDataResult);
                        CourseExt courseExt = courseDataResult.getValue();
                        Assert.assertNotNull(courseExt);
                        Assert.assertNotNull(courseExt.getMembers());
                        Assert.assertNotNull(courseExt.getProfile());
                    }
                }
        );

        ReadFuture.untilNonNull(dbref).get();

    }

}