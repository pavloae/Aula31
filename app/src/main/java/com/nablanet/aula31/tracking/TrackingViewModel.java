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
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.entity.ClassTrack;
import com.nablanet.aula31.repo.entity.Observation;
import com.nablanet.aula31.repo.entity.Profile;
import com.nablanet.aula31.repo.entity.MemberTrack;

import java.util.HashMap;
import java.util.Map;

public class TrackingViewModel extends ViewModel {

    private FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    private MutableLiveData<Profile> profileMutableLiveData;
    private MutableLiveData<Observation> currentObservationMutableLiveData;
    private MutableLiveData<Float> averageRateMutableLiveData;

    private MutableLiveData<DatabaseError> databaseErrorMutableLiveData = new MutableLiveData<>();

    LiveData<DatabaseError> getDatabaseError() {
        if (databaseErrorMutableLiveData == null)
            databaseErrorMutableLiveData = new MutableLiveData<>();
        return databaseErrorMutableLiveData;
    }

    LiveData<Profile> getCurrentProfileLiveData(@NonNull String memberId) {
        if (profileMutableLiveData == null) {
            profileMutableLiveData = new MutableLiveData<>();
            loadMemberTrackList(memberId);
        }
        return profileMutableLiveData;
    }

    LiveData<Observation> getCurrentObservationLiveData(@NonNull String memberId, @NonNull String classId) {
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

    void saveTrack(String memberId, String classId, String userId, Observation observation) {
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

    void updateMember(MemberTracks memberTracks) {

        Map<String, Object> childsUpdate = new HashMap<>();

        childsUpdate.put(
                "courses/" + memberTracks.course_id + "/members/" + memberTracks.id + "/lastname",
                memberTracks.profile.getLastname()
        );
        childsUpdate.put(
                "courses/" + memberTracks.course_id + "/members/" + memberTracks.id + "/names",
                memberTracks.profile.getNames()
        );
        childsUpdate.put(
                "tracking/" + memberTracks.id + "/profile/lastname", memberTracks.profile.getLastname()
        );
        childsUpdate.put(
                "tracking/" + memberTracks.id + "/profile/names", memberTracks.profile.getNames()
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
        fireBaseRepo.getMemberTrack(memberId).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        MemberTrack memberTrack = dataSnapshot.getValue(MemberTrack.class);
                        if (memberTrack == null) return;
                        profileMutableLiveData.setValue(memberTrack.profile);
                        getAverageRateLiveData().setValue(
                                getAverageRate(memberTrack, FirebaseAuth.getInstance().getUid())
                        );
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseErrorMutableLiveData.setValue(databaseError);
                    }
                }
        );
    }

    private Float getAverageRate(MemberTrack memberTrack, @Nullable String observerUid) {

        int count = 0;
        Float totalRate = 0F;

        for (ClassTrack classTrack : memberTrack.classes.values())
            for (String observer : classTrack.observations.keySet()){
                Observation observation = classTrack.observations.get(observer);
                if (observation != null && (observerUid == null || observer.equals(observerUid))) {
                    count++;
                    totalRate += observation.rate;
                }
            }

        return totalRate / count;
    }

    private void loadObservation(@NonNull String memberId, @NonNull String classId) {
        FirebaseDatabase.getInstance().getReference("tracking").child(memberId)
                .child("classes").child(classId).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ClassTrack classTrack = dataSnapshot.getValue(ClassTrack.class);
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
