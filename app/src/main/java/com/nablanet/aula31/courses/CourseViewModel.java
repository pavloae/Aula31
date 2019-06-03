package com.nablanet.aula31.courses;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nablanet.aula31.courses.entity.CourseProfileExt;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.entity.Membership;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseViewModel extends ViewModel {

    static final int COURSE_SAVED = 0;
    static final int COURSE_UPDATED = 1;

    FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    private MutableLiveData<CourseProfileExt> selectedCourseProfileMutableLiveData;
    private MutableLiveData<List<Membership>> membershipsMapMutableLiveData;

    private MutableLiveData<Response> responseMutableLiveData = new MutableLiveData<>();

    LiveData<CourseProfileExt> getSelectedCourseProfile() {
        if (selectedCourseProfileMutableLiveData == null) selectedCourseProfileMutableLiveData = new MutableLiveData<>();
        return selectedCourseProfileMutableLiveData;
    }

    LiveData<List<Membership>> getMembershipList() {
        if (membershipsMapMutableLiveData == null) {
            membershipsMapMutableLiveData = new MutableLiveData<>();
            loadUserMemberships();
        }
        return membershipsMapMutableLiveData;
    }

    LiveData<Response> getResponseLiveData() {
        return responseMutableLiveData;
    }

    private void loadUserMemberships() {
        fireBaseRepo.getOwnMemberships().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Membership> membershipList = new ArrayList<>();
                Membership membership;
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    membership = data.getValue(Membership.class);
                    if (membership == null) continue;
                    membership.membership_id = data.getKey();
                    membershipList.add(membership);
                }
                membershipsMapMutableLiveData.setValue(membershipList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                responseMutableLiveData.setValue(
                        new Response(false, databaseError.getMessage())
                );
            }
        });
    }

    void loadCourseProfile(@Nullable final String courseId) {
        if (TextUtils.isEmpty(courseId))
            selectedCourseProfileMutableLiveData.setValue(new CourseProfileExt());
        else {
            FirebaseDatabase.getInstance().getReference("courses").child(courseId).child("profile")
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    CourseProfileExt courseProfile = dataSnapshot.getValue(CourseProfileExt.class);
                                    if (courseProfile != null) courseProfile.setKey(courseId);
                                        selectedCourseProfileMutableLiveData.setValue(courseProfile);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    responseMutableLiveData.setValue(
                                            new Response(false, databaseError.getMessage())
                                    );
                                }
                            }
                    );
        }
    }

    void leaveCourse(Membership membership) {
        FirebaseDatabase.getInstance().getReference("memberships").child(membership.membership_id)
                .setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError,
                                           @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null)
                            responseMutableLiveData.setValue(
                                    new Response(false, databaseError.getMessage())
                            );
                    }
                });
    }

    /**
     * Método llamado para crear un curso nuevo a partir de las características indicadas
     * @param courseProfileExt Características del nuevo curso
     */
    void saveCourse(final CourseProfileExt courseProfileExt) {

        fireBaseRepo.saveNewCourse(courseProfileExt)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        createOwnerMembership(courseProfileExt);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        responseMutableLiveData.setValue(
                                new Response(false, e.getMessage())
                        );
                    }
                });
    }

    private void createOwnerMembership(CourseProfileExt courseProfile) {
        Membership membership = new Membership();
        membership.user_id = FirebaseAuth.getInstance().getUid();
        membership.course_id = courseProfile.getKey();
        membership.course_name = courseProfile.getCourseName();
        membership.institution_name = courseProfile.getInstitutionName();
        membership.role = Membership.TEACHER;
        membership.state = Membership.ACCEPTED;

        fireBaseRepo.createMembership(membership)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        responseMutableLiveData.setValue(
                                new Response(true, COURSE_SAVED, "")
                        );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        responseMutableLiveData.setValue(
                                new Response(false, e.getMessage())
                        );
                    }
                });
    }

    void updateCourseProfile(final String courseKey, final CourseProfileExt courseProfileExt) {

        FirebaseDatabase.getInstance().getReference("memberships")
                .orderByChild("courseId").equalTo(courseKey).limitToFirst(50)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Membership> membershipMap = new HashMap<>();
                        for (DataSnapshot data : dataSnapshot.getChildren())
                            membershipMap.put(data.getKey(), data.getValue(Membership.class));

                        courseProfileExt.setKey(courseKey);
                        updateChilds(courseProfileExt, membershipMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        responseMutableLiveData.setValue(
                                new Response(false, databaseError.getMessage())
                        );
                    }
                });
    }

    private void updateChilds(CourseProfileExt courseProfileExt, Map<String, Membership> membershipMap) {

        Map<String, Object> childsUpdate = new HashMap<>();
        childsUpdate.put("/courses/" + courseProfileExt.getKey() + "/profile", courseProfileExt.toMap());

        for (String key : membershipMap.keySet()){
            membershipMap.get(key).institution_name = courseProfileExt.getInstitutionName();
            membershipMap.get(key).course_name = courseProfileExt.getCourseName();
            childsUpdate.put("memberships/" + key, membershipMap.get(key).toMap());
        }

        FirebaseDatabase.getInstance().getReference().updateChildren(
                childsUpdate, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(
                            @Nullable DatabaseError databaseError,
                            @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null)
                            responseMutableLiveData.setValue(
                                    new Response(false, databaseError.getMessage())
                            );
                        else
                            responseMutableLiveData.setValue(
                                    new Response(true, COURSE_UPDATED, "")
                            );

                    }
                }
        );

    }

}
