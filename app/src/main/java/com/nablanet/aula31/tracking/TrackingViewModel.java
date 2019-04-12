package com.nablanet.aula31.tracking;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TrackingViewModel extends ViewModel {

    private MutableLiveData<MemberTrack.Profile> profileMutableLiveData;
    private MutableLiveData<MemberTrack.Observation> currentObservationMutableLiveData;
    private MutableLiveData<Float> averageRateMutableLiveData;

    private MutableLiveData<DatabaseError> databaseErrorMutableLiveData = new MutableLiveData<>();

    LiveData<DatabaseError> getDatabaseError() {
        if (databaseErrorMutableLiveData == null)
            databaseErrorMutableLiveData = new MutableLiveData<>();
        return databaseErrorMutableLiveData;
    }

    LiveData<MemberTrack.Profile> getCurrentProfileLiveData(@NonNull String memberId) {
        if (profileMutableLiveData == null) {
            profileMutableLiveData = new MutableLiveData<>();
            loadMemberTrackList(memberId);
        }
        return profileMutableLiveData;
    }

    LiveData<MemberTrack.Observation> getCurrentObservationLiveData(@NonNull String memberId, @NonNull String classId) {
        if (currentObservationMutableLiveData == null) {
            currentObservationMutableLiveData = new MutableLiveData<>();
            loadObservation(memberId, classId);
        }
        return currentObservationMutableLiveData;
    }

    MutableLiveData<Float> getAverageRateLiveData(){
        if (averageRateMutableLiveData == null) averageRateMutableLiveData = new MutableLiveData<>();
        return averageRateMutableLiveData;
    }

    void saveTrack(String memberId, String classId, String userId, MemberTrack.Observation observation) {
        FirebaseDatabase.getInstance().getReference("tracking").child(memberId)
                .child("classes").child(classId).child("observations").child(userId)
                .setValue(observation, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(
                            @Nullable DatabaseError databaseError,
                            @NonNull DatabaseReference databaseReference
                    ) {
                        if (databaseError != null)
                            databaseErrorMutableLiveData.setValue(databaseError);
                    }
                });
    }

    void updateMember(MemberTrack memberTrack) {

        Map<String, Object> childsUpdate = new HashMap<>();

        childsUpdate.put(
                "courses/" + memberTrack.course_id + "/members/" + memberTrack.id + "/lastname",
                memberTrack.profile.lastname
        );
        childsUpdate.put(
                "courses/" + memberTrack.course_id + "/members/" + memberTrack.id + "/names",
                memberTrack.profile.names
        );
        childsUpdate.put(
                "tracking/" + memberTrack.id + "/profile/lastname", memberTrack.profile.lastname
        );
        childsUpdate.put(
                "tracking/" + memberTrack.id + "/profile/names", memberTrack.profile.names
        );


        FirebaseDatabase.getInstance().getReference().updateChildren(
                childsUpdate, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(
                            @Nullable DatabaseError databaseError,
                            @NonNull DatabaseReference databaseReference
                    ) {
                        if (databaseError != null)
                            databaseErrorMutableLiveData.setValue(databaseError);
                    }
                }
        );

    }

    private void loadMemberTrackList(@NonNull final String memberId) {
        FirebaseDatabase.getInstance().getReference("tracking").child(memberId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        MemberTrack memberTrack = dataSnapshot.getValue(MemberTrack.class);
                        if (memberTrack == null) return;
                        profileMutableLiveData.setValue(memberTrack.profile);

                        getAverageRateLiveData().setValue(
                                memberTrack.getAverageRate(FirebaseAuth.getInstance().getUid())
                        );

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseErrorMutableLiveData.setValue(databaseError);
                    }
                });
    }

    private void loadObservation(@NonNull String memberId, @NonNull String classId) {
        FirebaseDatabase.getInstance().getReference("tracking").child(memberId)
                .child("classes").child(classId).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        MemberTrack.ClassTrack classTrack = dataSnapshot.getValue(MemberTrack.ClassTrack.class);
                        if (classTrack == null || classTrack.observations == null) return;
                        currentObservationMutableLiveData.setValue(
                                classTrack.observations.get(FirebaseAuth.getInstance().getUid())
                        );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseErrorMutableLiveData.setValue(databaseError);
                    }
                }
        );

    }



}
