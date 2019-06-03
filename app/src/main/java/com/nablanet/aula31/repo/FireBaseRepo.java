package com.nablanet.aula31.repo;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.repo.entity.Course;
import com.nablanet.aula31.repo.entity.CourseProfile;
import com.nablanet.aula31.repo.entity.CourseWork;
import com.nablanet.aula31.repo.entity.KeyImpl;
import com.nablanet.aula31.repo.entity.Member;
import com.nablanet.aula31.repo.entity.MemberRepo;
import com.nablanet.aula31.repo.entity.MemberTrack;
import com.nablanet.aula31.repo.entity.Membership;

import java.util.HashMap;
import java.util.Map;

public class FireBaseRepo {

    private static FireBaseRepo instance;

    public static FireBaseRepo getInstance() {
        if (instance == null)
            instance = new FireBaseRepo();
        return instance;
    }

    public FireBaseRepo() {
    }

    // membership

    public Query getOwnMemberships() {
        return FirebaseDatabase.getInstance().getReference("memberships")
                .orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getUid())
                .limitToFirst(100);
    }

    public Task<Void> createMembership(Membership membership) {
        return FirebaseDatabase.getInstance().getReference("memberships").push()
                .setValue(membership);
    }

    public Task<Void> saveNewCourse(CourseProfile courseProfile) {
        Course course = new Course();
        course.setProfile(courseProfile);
        return FirebaseDatabase.getInstance().getReference("courses").push().setValue(course);
    }

    public Task<Void> createClassDay(ClassDay classDay) {
        return FirebaseDatabase.getInstance().getReference("classes").push().setValue(classDay);
    }

    public LiveData<Course> getCourse(String courseId) {
        return Transformations.map(
                new FirebaseQueryLiveData<>(
                        FirebaseDatabase.getInstance().getReference("courses").child(courseId),
                        KeyImpl.class
                ),
                new Function<DataResult<KeyImpl>, Course>() {
                    @Override
                    public Course apply(DataResult dataResult) {
                        if (dataResult == null) return null;
                        return dataResult.getDataSnapshot().getValue(Course.class);
                    }
                }
        );
    }

    public LiveData<Map<String, Member>> getCourseMembers(String courseId) {
        return Transformations.map(
                new FirebaseQueryLiveData<>(
                        FirebaseDatabase.getInstance()
                                .getReference("courses").child(courseId).child("members"),
                        KeyImpl.class
                ),
                new Function<DataResult<KeyImpl>, Map<String, Member>>() {
                    @Override
                    public Map<String, Member> apply(DataResult dataResult) {
                        Map<String, Member> memberMap = new HashMap<>();
                        for (DataSnapshot data : dataResult.getDataSnapshot().getChildren()){
                            String key = data.getKey();
                            Member member = data.getValue(Member.class);
                            if (key != null && member != null)
                                memberMap.put(key, member);
                        }
                        return memberMap;
                    }
                }
        );
    }

    public LiveData<DataResult<ClassDay>> getCourseClasses(String courseId) {
        return new FirebaseQueryLiveData<>(
                FirebaseDatabase.getInstance().getReference("classes")
                        .orderByChild("courseId").equalTo(courseId).limitToFirst(190),
                ClassDay.class
        );
    }

    public DatabaseReference getClasses(String courseId) {
        return FirebaseDatabase.getInstance().getReference("classes")
                .orderByChild("courseId").equalTo(courseId).limitToFirst(190).getRef();
    }

    public DatabaseReference getMemberTrack(String memberId) {
        return FirebaseDatabase.getInstance().getReference("tracking").child(memberId);
    }

    public LiveData<DataResult<MemberTrack>> getTrackByCourse(String courseId) {
        return new FirebaseQueryLiveData<>(
                FirebaseDatabase.getInstance().getReference("tracking")
                        .orderByChild("courseId").equalTo(courseId).limitToFirst(30),
                MemberTrack.class
        );
    }

    public LiveData<DataResult<MemberRepo>> getMembersRepositories(String courseId) {
        return new FirebaseQueryLiveData<>(
                FirebaseDatabase.getInstance()
                        .getReference("works").orderByChild("courseId")
                        .equalTo(courseId).limitToFirst(30),
                MemberRepo.class
        );
    }

    public LiveData<DataResult<CourseWork>> getCourseWorks(String courseId) {
        return new FirebaseQueryLiveData<>(
                FirebaseDatabase.getInstance()
                        .getReference("repository").orderByChild("courseId")
                        .equalTo(courseId).limitToFirst(30),
                CourseWork.class
        );
    }

    public Task<Void> deleteMemberFromCourse(String courseId, String memberId) {
        return FirebaseDatabase.getInstance().getReference("courses").child(courseId)
                .child("members").child(memberId).child("state").setValue(Member.INACTIVE);
    }

    public Task<Void> deleteMemberFromClass(String classId, String memberId) {
        return FirebaseDatabase.getInstance().getReference("classes").child(classId)
                .child("members").child(memberId).setValue(null);
    }

    public String getUid(){
        return FirebaseAuth.getInstance().getUid();
    }

}
