package com.nablanet.aula31.classes.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.nablanet.aula31.classes.Util;
import com.nablanet.aula31.classes.entity.MemberItem;
import com.nablanet.aula31.repo.AbsentLiveData;
import com.nablanet.aula31.repo.DataResult;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.entity.Attendance;
import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.repo.entity.CourseProfile;
import com.nablanet.aula31.repo.entity.Member;
import com.nablanet.aula31.utils.Sort;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ClassViewModel extends ViewModel {

    public static final String TAG = "ClassViewModel";

    FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    private final MutableLiveData<Response> response = new MutableLiveData<>();

    private final MutableLiveData<String> courseId = new MutableLiveData<>();

    private LiveData<CourseProfile> courseProfileLive;
    private LiveData<List<Member>> courseMembersLive;
    private LiveData<List<ClassDay>> classDayList;

    private MediatorLiveData<ClassDay> currentClassDay;

    private MediatorLiveData<List<MemberItem>> mergedMemberList;

    private MediatorLiveData<MemberItem> currentMemberLive;

    public void setCourseId(@NonNull String courseId) {
        this.courseId.setValue(courseId);
    }

    public LiveData<String> getCourseId() {
        return courseId;
    }

    public LiveData<Response> getResponse() {
        return response;
    }

    @NonNull
    public LiveData<CourseProfile> getCourseProfileLive() {
        if (courseProfileLive == null)
            courseProfileLive = Transformations.switchMap(
                    courseId, new Function<String, LiveData<CourseProfile>>() {
                        @Override
                        public LiveData<CourseProfile> apply(String courseId) {

                            if (courseId == null)
                                return new AbsentLiveData<>();

                            return Transformations.map(
                                    fireBaseRepo.getCourseProfile(courseId),
                                    new Function<DataResult<CourseProfile>, CourseProfile>() {
                                        @Override
                                        public CourseProfile apply(
                                                DataResult<CourseProfile> input
                                        ) {

                                            if (input == null)
                                                return null;

                                            return input.getValue();
                                        }
                                    }
                            );
                        }
                    }
            );
        return courseProfileLive;
    }

    @NonNull
    private LiveData<List<Member>> getCourseMembersLive() {
        if (courseMembersLive == null)
            courseMembersLive = Transformations.switchMap(
                    courseId, new Function<String, LiveData<List<Member>>>() {
                        @Override
                        public LiveData<List<Member>> apply(String courseId) {

                            if (courseId == null)
                                return new AbsentLiveData<>();

                            return Transformations.map(
                                    fireBaseRepo.getCourseMemberResult(courseId),
                                    new Function<DataResult<Member>, List<Member>>() {
                                        @Override
                                        public List<Member> apply(DataResult<Member> input) {

                                            if (input == null || input.getMap() == null)
                                                return null;

                                            return new ArrayList<>(input.getMap().values());
                                        }
                                    }
                            );
                        }
                    }
            );
        return courseMembersLive;
    }

    @NonNull
    private LiveData<List<ClassDay>> getClassDaysLive() {

        if (classDayList == null)

            classDayList = Transformations.switchMap(

                    courseId, new Function<String, LiveData<List<ClassDay>>>() {

                        @Override
                        public LiveData<List<ClassDay>> apply(String courseId) {

                            if (courseId == null)
                                return new AbsentLiveData<>();

                            return Transformations.map(
                                    fireBaseRepo.getClasses(courseId),
                                    new Function<DataResult<ClassDay>, List<ClassDay>>() {
                                        @Override
                                        public List<ClassDay> apply(DataResult<ClassDay> input) {

                                            if (input == null || input.getMap() == null)
                                                return null;

                                            List<ClassDay> classDayList = new ArrayList<>(
                                                    input.getMap().values()
                                            );

                                            Sort.byDate(classDayList);

                                            return classDayList;
                                        }
                                    }
                            );
                        }
                    }
            );

        return classDayList;

    }

    @NonNull
    public MutableLiveData<ClassDay> getCurrentClassDay() {

        if (currentClassDay == null) {

            currentClassDay = new MediatorLiveData<>();

            currentClassDay.addSource(

                    getClassDaysLive(), new Observer<List<ClassDay>>() {

                        @Override
                        public void onChanged(@Nullable List<ClassDay> classDayList) {

                            // Cuando hay un cambio en la lista verificamos si se encuentra la
                            // clase activa. Si no, elegimos la última clase o la anulamos
                            // si la lista está vacía.

                            if (classDayList == null || classDayList.size() == 0) {

                                currentClassDay.setValue(null);

                                return;

                            } else if (currentClassDay.getValue() != null) {

                                for (ClassDay classDay : classDayList)
                                    if (Util.isSameDay(
                                            classDay.getDate(), currentClassDay.getValue().getDate()
                                    )) {
                                        currentClassDay.setValue(classDay);
                                        return;
                                    }

                            }

                            currentClassDay.setValue(classDayList.get(classDayList.size() - 1));

                        }

                    }

            );

        }

        return currentClassDay;

    }

    @NonNull
    public LiveData<List<MemberItem>> getMergedMemberList() {

        if (mergedMemberList == null) {

            mergedMemberList = new MediatorLiveData<>();

            mergedMemberList.addSource(

                    getCourseMembersLive(), new Observer<List<Member>>() {

                        @Override
                        public void onChanged(@Nullable List<Member> members) {

                            ClassDay classDay = getCurrentClassDay().getValue();

                            if (classDay == null) {
                                mergedMemberList.setValue(null);
                                return;
                            }

                            Map<String, Attendance> attendanceMap = classDay.getMembers();

                            List<MemberItem> memberItems = Util.mergeLists(members, attendanceMap);

                            Sort.byFullName(memberItems);

                            mergedMemberList.setValue(memberItems);

                        }
                    }
            );

            mergedMemberList.addSource(

                    getCurrentClassDay(), new Observer<ClassDay>() {

                        @Override
                        public void onChanged(@Nullable ClassDay classDay) {

                            if (classDay == null) {
                                mergedMemberList.setValue(null);
                                return;
                            }

                            Map<String, Attendance> attendanceMap = classDay.getMembers();

                            List<MemberItem> memberItems = Util.mergeLists(
                                    getCourseMembersLive().getValue(),
                                    attendanceMap
                            );

                            Sort.byFullName(memberItems);

                            mergedMemberList.setValue(memberItems);

                        }
                    }
            );

        }

        return mergedMemberList;

    }

    @NonNull
    public MutableLiveData<MemberItem> getCurrentMember() {

        if (currentMemberLive == null) {

            currentMemberLive = new MediatorLiveData<>();

            currentMemberLive.addSource(
                    getMergedMemberList(), new Observer<List<MemberItem>>() {
                        @Override
                        public void onChanged(@Nullable List<MemberItem> memberItems) {

                            MemberItem currentMember = currentMemberLive.getValue();

                            if (memberItems == null || memberItems.size() == 0)

                                currentMemberLive.setValue(null);

                            else if (currentMember == null || currentMember.getKey() == null) {

                                currentMemberLive.setValue(memberItems.get(0));

                            } else {

                                for (MemberItem member : memberItems) {
                                    if (currentMember.getKey().equals(member.getKey())) {
                                        currentMemberLive.setValue(member);
                                        return;
                                    }
                                }

                                currentMemberLive.setValue(memberItems.get(0));

                            }
                        }
                    }
            );

        }

        return currentMemberLive;

    }

    public void setNextMember() {

        List<MemberItem> memberItems = getMergedMemberList().getValue();
        if (memberItems == null || memberItems.size() == 0) {
            getCurrentMember().setValue(null);
            return;
        }

        MemberItem currentMember = getCurrentMember().getValue();
        if (currentMember == null)

            getCurrentMember().setValue(memberItems.get(0));

        else {

            int pos = memberItems.indexOf(currentMember);

            if (pos > -1 && pos < memberItems.size() - 1)

                getCurrentMember().setValue(memberItems.get(pos + 1));
        }

    }

    public void setPreviousMember() {

        List<MemberItem> memberItems = getMergedMemberList().getValue();
        if (memberItems == null || memberItems.size() == 0) {
            getCurrentMember().setValue(null);
            return;
        }

        MemberItem memberItem = getCurrentMember().getValue();
        if (memberItem == null)

            getCurrentMember().setValue(memberItems.get(0));

        else if (memberItems.indexOf(memberItem) > 0)

            getCurrentMember().setValue(memberItems.get(memberItems.indexOf(memberItem) - 1));

    }

    public void setNextClass() {

        List<ClassDay> classDayList = getClassDaysLive().getValue();
        if (classDayList == null || classDayList.size() == 0) {
            getCurrentClassDay().setValue(null);
            return;
        }

        ClassDay classDay = getCurrentClassDay().getValue();
        if (classDay == null)

            getCurrentClassDay().setValue(classDayList.get(classDayList.size() - 1));

        else if (classDayList.indexOf(classDay) < classDayList.size() - 1)

            getCurrentClassDay().setValue(classDayList.get(classDayList.indexOf(classDay) + 1));

    }

    public void setPreviousClass() {

        List<ClassDay> classDayList = getClassDaysLive().getValue();
        if (classDayList == null || classDayList.size() == 0) {
            getCurrentClassDay().setValue(null);
            return;
        }

        ClassDay classDay = getCurrentClassDay().getValue();
        if (classDay == null)

            getCurrentClassDay().setValue(classDayList.get(classDayList.size() - 1));

        else if (classDayList.indexOf(classDay) > 0)

            getCurrentClassDay().setValue(classDayList.get(classDayList.indexOf(classDay) - 1));

    }

    public void onDateSelected(int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        ClassDay newClassDay = new ClassDay(
                courseId.getValue(), calendar.getTimeInMillis(),null,null
        );

        // Revisamos que no exista una clase en el mismo día para este curso. Si existe la
        // mostramos como actual
        List<ClassDay> classDayList = getClassDaysLive().getValue();
        if (classDayList != null)
            for (ClassDay classDay : classDayList)
                if (Util.isSameDay(newClassDay.getDate(), classDay.getDate())) {
                    getCurrentClassDay().setValue(classDay);
                    return;
                }

        getCurrentClassDay().setValue(newClassDay);

        getCurrentMember().setValue(
                (getMergedMemberList().getValue() == null) ?
                null : getMergedMemberList().getValue().get(0)
        );

        fireBaseRepo.createClassDay(newClassDay).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.setValue(new Response(false, e.getMessage()));
                    }
                }
        );

    }

    public void setCurrentMember(MemberItem member) {

        getCurrentMember().setValue(member);

    }

    public void updateAttendance(@NonNull MemberItem member) {

        ClassDay classDay = getCurrentClassDay().getValue();

        if (classDay == null || classDay.getKey() == null || member.getKey() == null)
            return;

        fireBaseRepo.updateAttendance(classDay.getKey(), member.getKey(), member.getAttendance())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                });

    }

    /** Agregamos un nuevo miembro al curso y generamos una entrada en la lista de seguimientos
     *  para ir registrandolo en las siguientes clases
     *
     * @param member Un objeto con los datos del nuevo miembro
     */
    public void saveMemberToCourse(Member member) {

        String courseId = this.courseId.getValue();

        if (courseId == null)
            return;

        fireBaseRepo.createMember(courseId, member)
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                response.setValue(new Response(false, e.getMessage()));
            }
        });

    }

    public void deleteFromCourse(@NonNull MemberItem member) {

        String courseId = this.courseId.getValue();

        if (courseId == null || member.getKey() == null)
            return;

        fireBaseRepo.deleteMember(courseId, member.getKey())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.setValue(new Response(false, e.getMessage()));
                    }
                });

    }

    public void deleteFromClass(@NonNull MemberItem member) {

        ClassDay classDay = getCurrentClassDay().getValue();

        if (classDay == null || classDay.getKey() == null || member.getKey() == null)
            return;

        fireBaseRepo.updateAttendance(classDay.getKey(), member.getKey(), null)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.setValue(new Response(false, e.getMessage()));
                    }
                });

    }

}
