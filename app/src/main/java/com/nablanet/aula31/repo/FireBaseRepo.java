package com.nablanet.aula31.repo;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nablanet.aula31.courses.Course;

public class FireBaseRepo {

    public DatabaseReference getCourse(String courseId) {
        return FirebaseDatabase.getInstance().getReference("courses").child(courseId);
    }

    public Task<Void> deleteMemberFromCourse(String courseId, String memberId) {
        return FirebaseDatabase.getInstance().getReference("courses").child(courseId)
                .child("members").child(memberId).child("state").setValue(Course.Member.INACTIVE);
    }

}
