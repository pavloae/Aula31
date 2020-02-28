package com.nablanet.aula31.repo;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nablanet.aula31.repo.entity.Course;
import com.nablanet.aula31.repo.entity.CourseProfile;
import com.nablanet.aula31.repo.entity.Membership;
import com.nablanet.aula31.repo.query.WriteFuture;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

@RunWith(AndroidJUnit4.class)
public class FireBaseRepoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    public FirebaseDatabase dbInstance = FirebaseDatabase.getInstance();

    @Before
    public void setUp() throws Exception {

        if (Looper.myLooper() == null)
            Looper.prepare();

    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void createNewCourse() {

        String uid = FirebaseAuth.getInstance().getUid();

        System.out.println("UID: " + uid);

        CourseProfile courseProfile = new CourseProfile();
        courseProfile.setYear(2019);
        courseProfile.setOwner(uid);

        Course course = new Course();
        course.setProfile(courseProfile);

        DatabaseReference databaseReference = dbInstance.getReference("courses").push();
        //databaseReference.setValue(courseProfile);

        DatabaseError databaseError = null;
        try {
            databaseError = new WriteFuture(databaseReference, course).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (databaseError != null)
            System.out.println(databaseError.getMessage());
        else
            System.out.println(databaseReference.getKey());

    }


    @Test
    public void createMembership() {

        String uid = FirebaseAuth.getInstance().getUid();

        System.out.println("UID: " + uid);

        Membership membership = new Membership();
        membership.setUser_id(uid);
        membership.setCourse_id("-LgiUa0GkeT8wp9ofpPj");
        membership.setCourse_name("Materia 1");
        membership.setInstitution_name("Escuela 1");
        membership.setRole(Membership.TEACHER);
        membership.setState(Membership.ACCEPTED);

        DatabaseReference dbref = dbInstance.getReference("memberships").push();

        System.out.println("path: " + dbref.getPath());

        DatabaseError databaseError = null;
        try {
            databaseError = new WriteFuture(dbref, membership).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (databaseError != null)
            System.out.println(databaseError.getMessage());
        else
            System.out.println("DONE");

        Assert.assertNull(databaseError);

    }

}

