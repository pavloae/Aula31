package com.nablanet.aula31.classes;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nablanet.aula31.classes.entity.MemberItem;
import com.nablanet.aula31.repo.DataResult;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.entity.Attendance;
import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.repo.entity.Member;
import com.nablanet.aula31.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassViewModel extends ViewModel {

    public static final int MEMBER_CREATED = 0;
    public static final int MEMBER_EDITED = 1;
    public static final int MEMBER_DELETED = 2;
    public static final int CLASS_CREATED = 3;
    public static final int CLASS_EDITED = 4;
    public static final int CLASS_DELETED = 5;

    FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    private final MutableLiveData<Response> responseMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<String> courseId;

    private MediatorLiveData<List<ClassDay>> classDayList;
    private MutableLiveData<ClassDay> classDay;

    private LiveData<Map<String, Member>> memberMap;
    private LiveData<Map<String, Attendance>> attendanceMap;

    private MediatorLiveData<List<MemberItem>> memberItemList;
    private MutableLiveData<MemberItem> memberItem;

    @NonNull
    LiveData<Response> getResponseLiveData() {
        return responseMutableLiveData;
    }

    @NonNull
    MutableLiveData<String> getCourseIdLiveData() {
        if (courseId == null)
            courseId = new MutableLiveData<>();
        return courseId;
    }

    void setCourseId(String courseId) {
        getCourseIdLiveData().setValue(courseId);
    }

    @NonNull
    LiveData<List<MemberItem>> getMemberItemList() {
        if (memberItemList == null) {
            memberItemList = new MediatorLiveData<>();
            memberItemList.addSource(
                    getMemberMap(), new Observer<Map<String, Member>>() {
                        @Override
                        public void onChanged(@Nullable Map<String, Member> memberMap) {
                            memberItemList.setValue(
                                    getMergeListClass(memberMap, getAttendanceMap().getValue())
                            );
                        }
                    }
            );
            memberItemList.addSource(
                    getAttendanceMap(), new Observer<Map<String, Attendance>>() {
                        @Override
                        public void onChanged(@Nullable Map<String, Attendance> attendanceMap) {
                            memberItemList.setValue(
                                    getMergeListClass(getMemberMap().getValue(), attendanceMap)
                            );
                        }
                    }
            );
        }
        return memberItemList;
    }

    private List<MemberItem> getMergeListClass(
            @Nullable Map<String, Member> memberMap, @Nullable Map<String, Attendance> attendanceMap
    ) {
        if (memberMap == null)
            memberMap = new HashMap<>();

        if (attendanceMap == null)
            attendanceMap = new HashMap<>();

        //Creamos una lista de asistencia nueva
        List<MemberItem> newMemberItemList = new ArrayList<>();

        // Recorremos la lista de miembros del curso que debería
        // contener a todos los que están en la lista de asistencia de la clase
        Member member;
        for (String key : memberMap.keySet())
            // Si el miembro se encuentra activo, o ya no está activo pero
            // fue listado en algún momento en la clase,
            // lo agregamos a la nueva lista
            if ((member = memberMap.get(key)) != null &&
                    (member.getState() == Member.ACTIVE ||
                            attendanceMap.containsKey(key))) {
                member.member_id = key;
                MemberItem memberItem = new MemberItem();
                memberItem.setMember(member);
                memberItem.setAttendance(attendanceMap.get(key));
                newMemberItemList.add(memberItem);
            }

        return newMemberItemList;
    }

    @NonNull
    LiveData<Map<String, Member>> getMemberMap() {
        if (memberMap == null) {
            memberMap = Transformations.switchMap(
                    getCourseIdLiveData(), new Function<String, LiveData<Map<String, Member>>>() {
                        @Override
                        public LiveData<Map<String, Member>> apply(String courseId) {
                            return fireBaseRepo.getCourseMembers(courseId);
                        }
                    }
            );
        }
        return memberMap;
    }

    @NonNull
    LiveData<Map<String, Attendance>> getAttendanceMap() {
        if (attendanceMap == null)
            attendanceMap = Transformations.map(
                    getClassDay(), new Function<ClassDay, Map<String, Attendance>>() {
                        @Override
                        public Map<String, Attendance> apply(ClassDay classDay) {
                            return classDay.members;
                        }
                    }
            );
        return attendanceMap;
    }

    @NonNull
    MutableLiveData<ClassDay> getClassDay() {
        if (classDay == null)
            classDay = new MutableLiveData<>();
        return classDay;
    }

    void setClassDay(ClassDay classDay) {
        getClassDay().setValue(classDay);
    }

    @NonNull
    LiveData<List<ClassDay>> getClassDayList() {
        if (classDayList == null) {
            classDayList = new MediatorLiveData<>();
            classDayList.addSource(
                    fireBaseRepo.getCourseClasses(courseId.getValue()),
                    new Observer<DataResult<ClassDay>>() {
                        @Override
                        public void onChanged(@Nullable DataResult<ClassDay> dataSnapshot) {
                            if (dataSnapshot == null || dataSnapshot.getMap() == null)
                                classDayList.setValue(null);
                            else
                                classDayList.setValue(
                                        new ArrayList<>(dataSnapshot.getMap().values())
                                );
                        }
                    }
            );
        }
        return classDayList;
    }

    @NonNull
    MutableLiveData<MemberItem> getMemberItem() {
        if (memberItem == null)
            memberItem = new MutableLiveData<>();
        return memberItem;
    }

    void setMemberItem(MemberItem memberItem) {
        getMemberItem().setValue(memberItem);
    }

    void setNextMember() {

        List<MemberItem> memberItems = getMemberItemList().getValue();
        if (memberItems == null || memberItems.size() == 0) {
            setMemberItem(null);
            return;
        }

        MemberItem memberItem = getMemberItem().getValue();
        if (memberItem == null)
            setMemberItem(memberItems.get(0));
        else if (memberItems.indexOf(memberItem) < memberItems.size() - 1)
            setMemberItem(memberItems.get(memberItems.indexOf(memberItem) + 1));

    }

    void setPreviousMember() {

        List<MemberItem> memberItems = getMemberItemList().getValue();
        if (memberItems == null || memberItems.size() == 0) {
            setMemberItem(null);
            return;
        }

        MemberItem memberItem = getMemberItem().getValue();
        if (memberItem == null)
            setMemberItem(memberItems.get(0));
        else if (memberItems.indexOf(memberItem) > 0)
            setMemberItem(memberItems.get(memberItems.indexOf(memberItem) - 1));

    }

    void setNextClass() {

        List<ClassDay> classDayList = getClassDayList().getValue();
        if (classDayList == null || classDayList.size() == 0) {
            setClassDay(null);
            return;
        }

        ClassDay classDay = getClassDay().getValue();
        if (classDay == null)
            setClassDay(classDayList.get(classDayList.size() - 1));
        else if (classDayList.indexOf(classDay) < classDayList.size() - 1)
            setClassDay(classDayList.get(classDayList.indexOf(classDay) + 1));

    }

    void setPreviousClass() {

        List<ClassDay> classDayList = getClassDayList().getValue();
        if (classDayList == null || classDayList.size() == 0) {
            setClassDay(null);
            return;
        }

        ClassDay classDay = getClassDay().getValue();
        if (classDay == null)
            setClassDay(classDayList.get(classDayList.size() - 1));
        else if (classDayList.indexOf(classDay) > 0)
            setClassDay(classDayList.get(classDayList.indexOf(classDay) - 1));

    }

    void onClassDayListUpdated(List<ClassDay> classDayList) {

        if (classDayList == null || classDayList.size() == 0) {
            setClassDay(null);
            return;
        }

        ClassDay currentClassDay = getClassDay().getValue();

        if (currentClassDay == null || getClassOnDay(currentClassDay.getDate()) == null)
            setClassDay(classDayList.get(classDayList.size() - 1));

    }

    private ClassDay getClassOnDay(long date) {
        List<ClassDay> classDayList = getClassDayList().getValue();
        if (classDayList == null) return null;
        for (ClassDay classDay : classDayList)
            if (Util.isSameDay(classDay.getDate(), date))
                return classDay;
        return null;
    }

    ////////////

    void createClassDay(int year, int month, int dayOfMonth) {

        String courseId = getCourseIdLiveData().getValue();
        if (TextUtils.isEmpty(courseId)){
            responseMutableLiveData.setValue(
                    new Response(false, "Curso es null")
            );
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        ClassDay newClassDay = new ClassDay(
                courseId, calendar.getTimeInMillis(),null,null
        );

        // Revisamos que no exista una clase en el mismo día para este curso. Si existe la
        // mostramos como actual
        if (getClassDayList().getValue() != null)
            for (ClassDay classDay : getClassDayList().getValue())
                if (Util.isSameDay(newClassDay.getDate(), classDay.getDate())) {
                    setClassDay(classDay);
                    return;
                }

        fireBaseRepo.createClassDay(newClassDay)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        responseMutableLiveData.setValue(
                                new Response(CLASS_CREATED, "Clase creada")
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

    void updateAttendance(MemberItem memberItem) {

        ClassDay classDay = getClassDay().getValue();
        if (classDay == null || TextUtils.isEmpty(classDay.class_id)){
            responseMutableLiveData.setValue(
                    new Response(false, "Clase es null")
            );
            return;
        }

        FirebaseDatabase.getInstance().getReference("classes").child(classDay.class_id)
                .child("members").child(memberItem.memberId)
                .setValue(memberItem.getAttendance());

    }

    /** Agregamos un nuevo miembro al curso y generamos una entrada en la lista de seguimientos
     *  para ir registrandolo en las siguientes clases
     *
     * @param memberItem Un objeto con los datos del nuevo miembro
     */
    void saveMemberToCourse(MemberItem memberItem) {

        String courseId = getCourseIdLiveData().getValue();
        if (TextUtils.isEmpty(courseId)) return;

        assert courseId != null;
        String memberKey = FirebaseDatabase.getInstance().getReference("courses")
                .child(courseId).child("members").push().getKey();

        Map<String, Object> childsUpdate = new HashMap<>();
        childsUpdate.put("/courses/" + courseId + "/members/" + memberKey, memberItem.getMember().toMap());
        childsUpdate.put("tracking/" + memberKey + "/user_id", memberItem.getUser_id());
        childsUpdate.put("tracking/" + memberKey + "/course_id", courseId);
        childsUpdate.put("tracking/" + memberKey + "/profile/url_image", memberItem.getMember().getUrl_image());
        childsUpdate.put("tracking/" + memberKey + "/profile/lastname", memberItem.getMember().getLastname());
        childsUpdate.put("tracking/" + memberKey + "/profile/names", memberItem.getMember().getNames());

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
                                    new Response(MEMBER_CREATED, "Miembro creado")
                            );
                    }
                }
        );

    }

    void deleteFromCourse(MemberItem memberItem) {
        fireBaseRepo.deleteMemberFromCourse(memberItem.courseId, memberItem.memberId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        responseMutableLiveData.setValue(
                                new Response(true, "Borrado")
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

    void deleteFromClass(MemberItem memberItem) {
        fireBaseRepo.deleteMemberFromCourse(memberItem.classId, memberItem.memberId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        responseMutableLiveData.setValue(
                                new Response(true, "Borrado")
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

}
