package com.nablanet.aula31.schedules.ui.schedule;

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
import com.nablanet.aula31.courses.entity.CourseProfileExt;
import com.nablanet.aula31.schedules.Schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScheduleViewModel extends ViewModel {

    public static final int SCHEDULE_SAVED = 0;

    MutableLiveData<List<Schedule>> schedulesLiveDataByUser;
    MutableLiveData<CourseProfileExt> currentCourseMutableLiveData;
    String userId;

    private final MutableLiveData<DatabaseError> databaseErrorMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> successMutableLiveData = new MutableLiveData<>();

    public LiveData<List<Schedule>> getSchedulesLiveDataByUser() {
        if (userId == null) userId = FirebaseAuth.getInstance().getUid();
        if (schedulesLiveDataByUser == null) schedulesLiveDataByUser = new MutableLiveData<>();
        if (!TextUtils.isEmpty(userId)) loadScheduleLiveDataByUser(userId);
        return schedulesLiveDataByUser;
    }

    public LiveData<CourseProfileExt> getCurrentCourseMutableLiveData() {
        if (currentCourseMutableLiveData == null) currentCourseMutableLiveData = new MutableLiveData<>();
        return currentCourseMutableLiveData;
    }

    private void loadScheduleLiveDataByUser(String userId) {
        FirebaseDatabase.getInstance().getReference("hours")
                .orderByChild("user_id").equalTo(userId).limitToFirst(20)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<Schedule> schedules = new ArrayList<>();
                                Schedule schedule;
                                for (DataSnapshot data : dataSnapshot.getChildren()){
                                    schedule = data.getValue(Schedule.class);
                                    if (schedule == null) continue;
                                    schedule.id = data.getKey();
                                    schedules.add(schedule);
                                }
                                schedulesLiveDataByUser.setValue(schedules);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                databaseErrorMutableLiveData.setValue(databaseError);
                            }
                        }
                );
    }

    public void saveHours(String scheduleId, Schedule.Hours hours) {

        DatabaseReference scheduleRef = FirebaseDatabase.getInstance().getReference("schedules");

        DatabaseReference.CompletionListener completionListener = new DatabaseReference
                .CompletionListener() {
            @Override
            public void onComplete(
                    @Nullable DatabaseError databaseError,
                    @NonNull DatabaseReference databaseReference
            ) {
                if (databaseError != null)
                    databaseErrorMutableLiveData.setValue(databaseError);

            }
        };

        if (TextUtils.isEmpty(scheduleId) && currentCourseMutableLiveData.getValue() != null) {
            Schedule schedule = new Schedule();
            scheduleRef = scheduleRef.push();
            schedule.id = scheduleRef.push().getKey();
            schedule.course_id = currentCourseMutableLiveData.getValue().getKey();
            schedule.user_id = FirebaseAuth.getInstance().getUid();
            schedule.weekdays = new HashMap<>();
            schedule.weekdays.put(
                    scheduleRef.child("weekdays").push().getKey(), hours
            );
            scheduleRef.setValue(schedule, completionListener);

        } else {
            scheduleRef.child(scheduleId).child("weekdays/" + hours.id)
                    .setValue(hours, completionListener);
        }
    }

    public void saveSchedule(Schedule schedule) {

        FirebaseDatabase.getInstance().getReference("hours").push()
                .setValue(schedule, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(
                            @Nullable DatabaseError databaseError,
                            @NonNull DatabaseReference databaseReference
                    ) {
                        if (databaseError != null)
                            databaseErrorMutableLiveData.setValue(databaseError);
                        else
                            successMutableLiveData.setValue(SCHEDULE_SAVED);
                    }
                });

    }



}
