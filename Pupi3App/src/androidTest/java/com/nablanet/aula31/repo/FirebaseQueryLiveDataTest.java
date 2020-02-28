package com.nablanet.aula31.repo;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.nablanet.aula31.export.entity.CourseExt;
import com.nablanet.aula31.repo.entity.Course;
import com.nablanet.aula31.repo.entity.Membership;
import com.nablanet.aula31.repo.query.ReadFuture;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                        System.out.println("Observer.Course");
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

        FirebaseQueryLiveData<Course> firebaseQueryLiveData =  new FirebaseQueryLiveData<>(
                dbref, Course.class
        );
        firebaseQueryLiveData.observeForever(
                new Observer<DataResult<Course>>() {
                    @Override
                    public void onChanged(@Nullable DataResult<Course> courseDataResult) {
                        System.out.println("Observer.CourseExt");
                        Assert.assertNotNull(courseDataResult);
                        CourseExt courseExt = new CourseExt(courseDataResult.getValue());
                        Assert.assertNotNull(courseExt);
                        Assert.assertNotNull(courseExt.getMembers());
                        Assert.assertNotNull(courseExt.getProfile());
                    }
                }
        );

        ReadFuture.untilNonNull(dbref).get();

    }

    @Test
    public void getMemberships() throws ExecutionException, InterruptedException {

        Query query = FirebaseDatabase.getInstance().getReference("memberships")
                .orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getUid())
                .limitToFirst(30);

        new FirebaseQueryLiveData<>(query, Membership.class)
                .observeForever(new Observer<DataResult<Membership>>() {
                    @Override
                    public void onChanged(@Nullable DataResult<Membership> membershipDataResult) {
                        Assert.assertNotNull(membershipDataResult);
                        Map<String, Membership> map = membershipDataResult.getMap();
                        Assert.assertNotNull(map);

                        List<Membership> membershipList = new ArrayList<>(membershipDataResult.getMap().values());
                        Assert.assertNotNull(membershipList);
                    }
                });

        ReadFuture.untilNonNull(query).get();

    }

}