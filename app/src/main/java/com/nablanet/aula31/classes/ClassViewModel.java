package com.nablanet.aula31.classes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nablanet.aula31.courses.Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassViewModel extends ViewModel {

    public static final int MEMBER_CREATED = 0;

    private MutableLiveData<Course> courseMutableLiveData;
    private MutableLiveData<ClassDay> classMutableLiveData;

    private MutableLiveData<List<ClassDay>> classDayListMutableLiveData;

    private MutableLiveData<DatabaseError> databaseErrorMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> successMutableLiveData = new MutableLiveData<>();

    LiveData<DatabaseError> getDatabaseError() {
        if (databaseErrorMutableLiveData == null)
            databaseErrorMutableLiveData = new MutableLiveData<>();
        return databaseErrorMutableLiveData;
    }

    @NonNull
    LiveData<Course> getCourseLiveData(@Nullable String courseId) {
        if (courseMutableLiveData == null)
            courseMutableLiveData = new MutableLiveData<>();

        if (courseId != null)
            loadCourse(courseId);

        return courseMutableLiveData;
    }

    @NonNull
    LiveData<ClassDay> getClassLiveData() {
        if (classMutableLiveData == null){
            classMutableLiveData = new MutableLiveData<>();
            loadClasses();
        }
        return classMutableLiveData;
    }

    @NonNull
    MutableLiveData<List<ClassDay>> getClassDayListLiveData() {
        if (classDayListMutableLiveData == null)
            classDayListMutableLiveData = new MutableLiveData<>();
        return classDayListMutableLiveData;
    }

    void setNextClass() {
        List<ClassDay> classDayList = getClassDayListLiveData().getValue();
        if (classDayList == null || classDayList.size() == 0)
            return;

        ClassDay currentClassDay = getClassLiveData().getValue();
        if (currentClassDay == null)
            setCurrentClass(classDayList.get(0));

        setCurrentClass(
                classDayList.get(
                        (classDayList.indexOf(currentClassDay) == classDayList.size() - 1) ?
                                0 : classDayList.indexOf(currentClassDay) + 1
                )
        );
    }

    void setPrevClass() {
        List<ClassDay> classDayList = getClassDayListLiveData().getValue();
        if (classDayList == null || classDayList.size() == 0)
            return;

        ClassDay currentClassDay = getClassLiveData().getValue();
        if (currentClassDay == null)
            setCurrentClass(classDayList.get(0));

        setCurrentClass(
                classDayList.get(
                        (classDayList.indexOf(currentClassDay) == 0) ?
                                classDayList.size() - 1 : classDayList.indexOf(currentClassDay) - 1
                )
        );
    }

    void saveNewClassDay(ClassDay newClassDay) {
        List<ClassDay> classDayList = getClassDayListLiveData().getValue();
        Course course = getCourseLiveData(null).getValue();
        if (course == null || TextUtils.isEmpty(course.id) || classDayList == null) return;

        for (ClassDay classDay : classDayList)
            if (newClassDay.isSameDay(classDay)) {
                setCurrentClass(classDay);
                return;
            }

        newClassDay.course_id = course.id;

        FirebaseDatabase.getInstance().getReference("classes").push()
                .setValue(newClassDay, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(
                            @Nullable DatabaseError databaseError,
                            @NonNull DatabaseReference databaseReference
                    ) {

                    }
                });

    }

    void updateMemberClass(ClassDay.Member member) {

        ClassDay classDay = getClassLiveData().getValue();
        if (classDay == null || TextUtils.isEmpty(classDay.id)) return;

        FirebaseDatabase.getInstance().getReference("classes").child(classDay.id)
                .child("members").child(member.id).setValue(member);

    }

    void saveMemberToCourse(Course.Member member) {

        Course course = getCourseLiveData(null).getValue();
        if (course == null || TextUtils.isEmpty(course.id)) return;

        String memberKey = FirebaseDatabase.getInstance().getReference("courses")
                .child(course.id).child("members").push().getKey();

        Map<String, Object> childsUpdate = new HashMap<>();
        childsUpdate.put("/courses/" + course.id + "/members/" + memberKey, member.toMap());
        childsUpdate.put("tracking/" + memberKey + "/user_id", member.user_id);
        childsUpdate.put("tracking/" + memberKey + "/course_id", course.id);
        childsUpdate.put("tracking/" + memberKey + "/profile/url_image", member.url_image);
        childsUpdate.put("tracking/" + memberKey + "/profile/lastname", member.lastname);
        childsUpdate.put("tracking/" + memberKey + "/profile/names", member.names);

        FirebaseDatabase.getInstance().getReference().updateChildren(
                childsUpdate, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(
                            @Nullable DatabaseError databaseError,
                            @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null)
                            databaseErrorMutableLiveData.setValue(databaseError);
                        else
                            successMutableLiveData.setValue(MEMBER_CREATED);
                    }
                }
        );

    }

    private void setCurrentClass(@Nullable ClassDay classDay) {
        if (classMutableLiveData == null)
            classMutableLiveData = new MutableLiveData<>();
        classMutableLiveData.setValue(classDay);
    }

    private void loadCourse(@NonNull String courseId) {
        FirebaseDatabase.getInstance().getReference("courses").child(courseId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Course course = dataSnapshot.getValue(Course.class);
                        if (course != null) course.id = dataSnapshot.getKey();
                        courseMutableLiveData.setValue(course);
                        loadClasses();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseErrorMutableLiveData.setValue(databaseError);
                    }
                });
    }

    private void loadClasses() {
        Course course = getCourseLiveData(null).getValue();
        if (course == null || TextUtils.isEmpty(course.id)) return;

        FirebaseDatabase.getInstance().getReference("classes")
                .orderByChild("course_id").equalTo(course.id).limitToFirst(190)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<ClassDay> classDays = new ArrayList<>();
                        ClassDay classDay;
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            classDay = data.getValue(ClassDay.class);
                            if (classDay == null) continue;
                            classDay.id = data.getKey();
                            classDays.add(classDay);
                        }

                        // Las clases quedan ordenadas de la más reciente a la más antigua
                        Collections.sort(classDays, new Comparator<ClassDay>() {
                            @Override
                            public int compare(ClassDay o1, ClassDay o2) {
                                return o2.getDate().compareTo(o1.getDate());
                            }
                        });

                        getClassDayListLiveData().setValue(classDays);

                        if (classDays.size() > 0) setCurrentClass(classDays.get(0));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseErrorMutableLiveData.setValue(databaseError);
                    }
                });
    }

}
