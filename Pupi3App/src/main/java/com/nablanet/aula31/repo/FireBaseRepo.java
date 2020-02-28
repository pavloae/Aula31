package com.nablanet.aula31.repo;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nablanet.aula31.repo.entity.Attendance;
import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.repo.entity.Course;
import com.nablanet.aula31.repo.entity.CourseProfile;
import com.nablanet.aula31.repo.entity.CourseWork;
import com.nablanet.aula31.repo.entity.KeyImpl;
import com.nablanet.aula31.repo.entity.Member;
import com.nablanet.aula31.repo.entity.MemberRepo;
import com.nablanet.aula31.repo.entity.MemberTrack;
import com.nablanet.aula31.repo.entity.Membership;
import com.nablanet.aula31.repo.entity.Observation;
import com.nablanet.aula31.repo.entity.Phone;
import com.nablanet.aula31.repo.entity.Profile;
import com.nablanet.aula31.repo.entity.User;

import java.util.HashMap;
import java.util.Map;

public class FireBaseRepo {

    private static FireBaseRepo instance;

    private final FirebaseDatabase dbInstance;

    public static FireBaseRepo getInstance() {
        if (instance == null)
            instance = new FireBaseRepo();
        return instance;
    }

    private FireBaseRepo() {
        dbInstance = FirebaseDatabase.getInstance();
    }

    public String getUid(){
        return FirebaseAuth.getInstance().getUid();
    }

    public String getOwnPhoneId() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return null;
        return FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
    }

    public LiveData<DataResult<User>> getOwnUser() {
        return new FirebaseQueryLiveData<>(
                dbInstance.getReference("users").child(getUid()), User.class
        );
    }

    public LiveData<DataResult<Phone>> getOwnPhone() {
        return new FirebaseQueryLiveData<>(
                dbInstance.getReference("phones").child(getOwnPhoneId()), Phone.class
        );
    }

    public void update(
            @Nullable User user, @Nullable Phone phone, @NonNull OnFailureListener onFailureListener
    ) {

        if (user == null && phone == null)
            return;

        Map<String, Object> childUpdates = new HashMap<>();

        if (user != null) {
            user.setKey(getUid());
            childUpdates.put("/users/" + user.getKey(), user.toMap());
        }

        if (phone != null) {
            phone.setKey(getOwnPhoneId());
            childUpdates.put("/phones/" + phone.getKey(), phone.toMap());
        }

        dbInstance.getReference()
                .updateChildren(childUpdates).addOnFailureListener(onFailureListener);

    }

    public LiveData<DataResult<Membership>> getMemberships() {
        return new FirebaseQueryLiveData<>(
                dbInstance.getReference("memberships")
                        .orderByChild("user_id").equalTo(getUid()).limitToFirst(30),
                Membership.class
        );
    }

    public LiveData<DataResult<CourseProfile>> getCourseProfile(@NonNull String courseId) {
        return new FirebaseQueryLiveData<>(
                dbInstance.getReference("courses").child(courseId).child("profile"),
                CourseProfile.class
        );
    }

    public void deleteMembership(@NonNull String membershipId) {
        dbInstance.getReference("memberships").child(membershipId).setValue(null);
    }

    public Task<Void> create(@NonNull Course course) {

        if (course.getProfile() == null)
            course.setProfile(new CourseProfile());

        course.getProfile().setOwner(FirebaseAuth.getInstance().getUid());

        DatabaseReference databaseReference = dbInstance.getReference("courses").push();

        course.setKey(databaseReference.getKey());

        return databaseReference.setValue(course);

    }

    public void updateCourseProfile(
            @NonNull final String courseId, @NonNull final CourseProfile courseProfile
    ) {

        final FirebaseQueryLiveData<Membership> firebaseQueryLiveData = new FirebaseQueryLiveData<>(
                dbInstance.getReference("memberships")
                        .orderByChild("course_id").equalTo(courseId).limitToFirst(50),
                Membership.class
        );

        firebaseQueryLiveData.observeForever(
                new Observer<DataResult<Membership>>() {
                    @Override
                    public void onChanged(@Nullable DataResult<Membership> dataResult) {
                        firebaseQueryLiveData.removeObserver(this);
                        updateChilds(
                                courseId,
                                courseProfile,
                                (dataResult == null) ? null : dataResult.getMap()
                        );
                    }
                }
        );

    }

    private void updateChilds(
            @NonNull String courseId,
            @NonNull CourseProfile courseProfile, @Nullable Map<String, Membership> membershipMap
    ) {

        Map<String, Object> childsUpdate = new HashMap<>();

        childsUpdate.put("/courses/" + courseId + "/profile", courseProfile.toMap());

        if (membershipMap != null)
            for (String key : membershipMap.keySet()){
                Membership membership = membershipMap.get(key);
                if (membership == null)
                    continue;

                membership.setCourse_name(Utils.getCourseName(courseProfile));
                membership.setInstitution_name(
                        courseProfile.getInstitution() == null ?
                                null : courseProfile.getInstitution().getName()
                );
                childsUpdate.put("memberships/" + key, membership.toMap());
            }

        dbInstance.getReference().updateChildren(childsUpdate);

    }

    public Task<Void> createMembership(Membership membership) {

        membership.setUser_id(FirebaseAuth.getInstance().getUid());

        return dbInstance.getReference("memberships").push().setValue(membership);

    }

    public Task<Void> createMember(@NonNull String courseId, @NonNull Member member) {

        String memberKey = FirebaseDatabase.getInstance().getReference("courses")
                .child(courseId).child("members").push().getKey();

        MemberTrack memberTrack = new MemberTrack();
        memberTrack.setKey(memberKey);
        memberTrack.setCourse_id(courseId);
        memberTrack.setProfile(member.getProfile());

        Map<String, Object> childsUpdate = new HashMap<>();
        childsUpdate.put("/courses/" + courseId + "/members/" + memberKey, member.toMap());
        childsUpdate.put("/tracking/" + memberKey, memberTrack.toMap());

        return dbInstance.getReference().updateChildren(childsUpdate);

    }

    public Task<Void> updateMemberProfile(
            @NonNull String memberId, @NonNull String courseId, Profile profile
    ) {

        Map<String, Object> childsUpdate = new HashMap<>();

        childsUpdate.put(
                "courses/" + courseId + "/members/" + memberId + "/profile", profile.toMap()
        );

        childsUpdate.put(
                "tracking/" + memberId + "/profile", profile.toMap()
        );


        return dbInstance.getReference().updateChildren(childsUpdate);

    }

    public LiveData<DataResult<ClassDay>> getClasses(String courseId) {
        return new FirebaseQueryLiveData<>(
                dbInstance.getReference("classes")
                        .orderByChild("course_id").equalTo(courseId).limitToFirst(190),
                ClassDay.class
        );
    }

    public Task<Void> updateAttendance(
            @NonNull String classId, @NonNull String memberId, @Nullable Attendance attendance
    ) {
        return dbInstance.getReference("classes").child(classId).child("members")
                .child(memberId).setValue(attendance);
    }

    public Task<Void> deleteMember(@NonNull String courseId, @NonNull String memberId) {
        return dbInstance.getReference("courses").child(courseId).child("members")
                .child(memberId).child("state").setValue(Member.INACTIVE);
    }

    public Task<Void> createClassDay(ClassDay classDay) {
        return FirebaseDatabase.getInstance().getReference("classes").push().setValue(classDay);
    }

    public LiveData<DataResult<MemberTrack>> getMemberTrack(@NonNull String memberId) {

        return new FirebaseQueryLiveData<>(
                dbInstance.getReference("tracking").child(memberId),
                MemberTrack.class
        );

    }

    public LiveData<DataResult<MemberTrack>> getTrackByCourse(@NonNull String courseId) {
        return new FirebaseQueryLiveData<>(
                FirebaseDatabase.getInstance().getReference("tracking")
                        .orderByChild("courseId").equalTo(courseId).limitToFirst(30),
                MemberTrack.class
        );
    }

    public Task<Void> updateTrack(
            @NonNull String memberId, @NonNull String classId, @Nullable Observation observation
    ) {

        return dbInstance.getReference("tracking").child(memberId)
                .child("classes").child(classId)
                .child("observations").child(getUid())
                .setValue(observation);

    }

    public void updateClassTrackDate(
            @NonNull String memberId, @NonNull String classId, @Nullable Long date
            ) {

        dbInstance.getReference("tracking").child(memberId)
                .child("classes").child(classId).child("date").setValue(date);

    }













    ////////////////////////////////////////

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

    public LiveData<DataResult<Member>> getCourseMemberResult(String courseId) {
        return new FirebaseQueryLiveData<>(
                dbInstance.getReference("courses").child(courseId).child("members"),
                Member.class
        );
    }

    public LiveData<DataResult<ClassDay>> getCourseClasses(String courseId) {
        return new FirebaseQueryLiveData<>(
                FirebaseDatabase.getInstance().getReference("classes")
                        .orderByChild("course_id").equalTo(courseId).limitToFirst(190),
                ClassDay.class
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



}
