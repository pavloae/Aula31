package com.nablanet.aula31.courses;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseViewModel extends ViewModel {

    static final int COURSE_SAVED = 0;
    static final int COURSE_UPDATED = 1;

    private MutableLiveData<Course.Profile> selectedCourseProfileMutableLiveData;
    private MutableLiveData<List<Membership>> membershipsMapMutableLiveData;
    private MutableLiveData<DatabaseError> databaseErrorMutableLiveData;
    private MutableLiveData<Integer> successMutableLiveData;

    LiveData<Course.Profile> getSelectedCourseProfile() {
        if (selectedCourseProfileMutableLiveData == null) selectedCourseProfileMutableLiveData = new MutableLiveData<>();
        return selectedCourseProfileMutableLiveData;
    }

    LiveData<List<Membership>> getMemberships() {
        if (membershipsMapMutableLiveData == null) {
            membershipsMapMutableLiveData = new MutableLiveData<>();
            loadUserMemberships();
        }
        return membershipsMapMutableLiveData;
    }

    LiveData<DatabaseError> getDatabaseError() {
        if (databaseErrorMutableLiveData == null)
            databaseErrorMutableLiveData = new MutableLiveData<>();
        return databaseErrorMutableLiveData;
    }

    LiveData<Integer> getSuccessMutableLiveData() {
        if (successMutableLiveData == null)
            successMutableLiveData = new MutableLiveData<>();
        return successMutableLiveData;
    }

    void loadCourseProfile(@Nullable final String courseId) {
        if (TextUtils.isEmpty(courseId))
            selectedCourseProfileMutableLiveData.setValue(new Course.Profile());
        else {
            FirebaseDatabase.getInstance().getReference("courses").child(courseId).child("profile")
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Course.Profile profile = dataSnapshot.getValue(Course.Profile.class);
                                    if (profile != null) profile.courseId = courseId;
                                        selectedCourseProfileMutableLiveData.setValue(profile);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    databaseErrorMutableLiveData.setValue(databaseError);
                                }
                            }
                    );
        }
    }

    void leaveCourse(Membership membership) {
        FirebaseDatabase.getInstance().getReference("memberships").child(membership.id)
                .setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null)
                            databaseErrorMutableLiveData.setValue(databaseError);
                    }
                });
    }

    /**
     * Método llamado para crear un curso nuevo a partir de las características indicadas
     * @param courseProfile Características del nuevo curso
     */
    void saveCourse(final Course.Profile courseProfile) {

        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference()
                .child("courses").push();

        Course course = new Course();
        course.profile = courseProfile;
        course.profile.courseId = courseRef.getKey();

        courseRef.setValue(course, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(
                    @Nullable DatabaseError databaseError,
                    @NonNull DatabaseReference databaseReference
            ) {
                if (databaseError != null)
                    databaseErrorMutableLiveData.setValue(databaseError);
                else
                    createOwnerMembership(courseProfile);

            }
        });
    }

    private void createOwnerMembership(Course.Profile profile) {
        Membership membership = new Membership();
        membership.user_id = FirebaseAuth.getInstance().getUid();
        membership.course_id = profile.courseId;
        membership.institution_name = profile.getInstitutionName();
        membership.course_name = profile.getCourseName();
        membership.role = Membership.TEACHER;
        membership.state = Membership.ACCEPTED;

        FirebaseDatabase.getInstance().getReference("memberships").push()
                .setValue(membership, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(
                            @Nullable DatabaseError databaseError,
                            @NonNull DatabaseReference databaseReference
                    ) {
                        if (databaseError != null)
                            databaseErrorMutableLiveData.setValue(databaseError);
                        else
                            successMutableLiveData.setValue(COURSE_SAVED);
                    }
                });

    }

    void updateCourseProfile(final String courseKey, final Course.Profile courseProfile) {

        FirebaseDatabase.getInstance().getReference("memberships")
                .orderByChild("course_id").equalTo(courseKey).limitToFirst(50)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Membership> membershipMap = new HashMap<>();
                        for (DataSnapshot data : dataSnapshot.getChildren())
                            membershipMap.put(data.getKey(), data.getValue(Membership.class));

                        courseProfile.courseId = courseKey;
                        updateChilds(courseProfile, membershipMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseErrorMutableLiveData.setValue(databaseError);
                    }
                });
    }

    private void updateChilds(Course.Profile courseProfile, Map<String, Membership> membershipMap) {

        Map<String, Object> childsUpdate = new HashMap<>();
        childsUpdate.put("/courses/" + courseProfile.courseId + "/profile", courseProfile.toMap());

        for (String key : membershipMap.keySet()){
            membershipMap.get(key).institution_name = courseProfile.getInstitutionName();
            membershipMap.get(key).course_name = courseProfile.getCourseName();
            childsUpdate.put("memberships/" + key, membershipMap.get(key).toMap());
        }

        FirebaseDatabase.getInstance().getReference().updateChildren(
                childsUpdate, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(
                            @Nullable DatabaseError databaseError,
                            @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null)
                            databaseErrorMutableLiveData.setValue(databaseError);
                        else
                            successMutableLiveData.setValue(COURSE_UPDATED);

                    }
                }
        );

    }

    private void loadUserMemberships() {
        FirebaseDatabase.getInstance().getReference("memberships")
                .orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getUid()).limitToFirst(100)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Membership> membershipList = new ArrayList<>();
                        Membership membership;
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            membership = data.getValue(Membership.class);
                            if (membership == null) continue;
                            membership.id = data.getKey();
                            membershipList.add(membership);

                        }
                        membershipsMapMutableLiveData.setValue(membershipList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseErrorMutableLiveData.setValue(databaseError);
                    }
                });
    }

}
