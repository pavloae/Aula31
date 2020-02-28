package com.nablanet.aula31.tracking.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.nablanet.aula31.classes.Util;
import com.nablanet.aula31.repo.AbsentLiveData;
import com.nablanet.aula31.repo.DataResult;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.repo.entity.ClassTrack;
import com.nablanet.aula31.repo.entity.Observation;
import com.nablanet.aula31.repo.entity.Profile;
import com.nablanet.aula31.repo.entity.MemberTrack;
import com.nablanet.aula31.utils.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrackingViewModel extends ViewModel {

    private FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    private MutableLiveData<Response> response = new MutableLiveData<>();

    private MutableLiveData<String> currentMember = new MutableLiveData<>();

    private MediatorLiveData<ClassTrack> classTrackLive;

    private LiveData<DataResult<MemberTrack>> memberTrack;

    private MediatorLiveData<Profile> profile;

    private MediatorLiveData<List<ClassTrack>> classTrackList;

    private MediatorLiveData<Float> average;

    private MediatorLiveData<Observation> observation;

    ///////////// Metodo temporario para corregir las fechas faltantes en ClassTrack //////////////

    private LiveData<DataResult<ClassDay>> courseClassList;

    @NonNull
    private LiveData<DataResult<ClassDay>> getCourseClassList() {
        if (courseClassList == null) {
            courseClassList = Transformations.switchMap(
                    getMemberTrackLive(), new Function<DataResult<MemberTrack>, LiveData<DataResult<ClassDay>>>() {
                        @Override
                        public LiveData<DataResult<ClassDay>> apply(DataResult<MemberTrack> input) {

                            if (input != null && input.getDatabaseError() != null)
                                response.setValue(
                                        new Response(
                                                false,
                                                input.getDatabaseError().getMessage()
                                        )
                                );


                            if (
                                    input != null &&
                                            input.getValue() != null &&
                                            input.getValue().getCourse_id() != null
                            )

                                return fireBaseRepo.getCourseClasses(
                                        input.getValue().getCourse_id()
                                );

                            else

                                return new AbsentLiveData<>();

                        }
                    }
            );
        }
        return courseClassList;
    }

    private void addSource(MediatorLiveData<List<ClassTrack>> classTrackList) {

        classTrackList.addSource(
                getCourseClassList(), new Observer<DataResult<ClassDay>>() {
                    @Override
                    public void onChanged(@Nullable DataResult<ClassDay> dataResult) {

                        if (dataResult != null && dataResult.getDatabaseError() != null)

                            response.setValue(
                                    new Response(
                                            false,
                                            dataResult.getDatabaseError().getMessage()
                                    )
                            );

                        else

                            mixDates();

                    }
                }
        );

    }

    private void mixDates() {

        if (getCourseClassList().getValue() == null)
            return;

        Map<String, ClassDay> classDayMap = getCourseClassList().getValue().getMap();
        if (classDayMap == null)
            return;

        ClassTrack currentClass = getCurrentClass().getValue();
        if (currentClass != null && currentClass.getDate() == null) {
            String classId = currentClass.getKey();
            ClassDay currentClassDay = classDayMap.get(classId);
            if (currentClassDay != null) {
                currentClass.setDate(currentClassDay.getDate());
                getCurrentClass().setValue(currentClass);
            }
        }

        List<ClassTrack> classTracks = getClassTrackList().getValue();
        if (classTracks == null || classTracks.size() == 0)
            return;

        String memberId;
        if (getMemberTrack() == null || (memberId = getMemberTrack().getKey()) == null)
            return;

        ClassDay classDay;
        for (ClassTrack classTrack : classTracks) {

            String classId = classTrack.getKey();
            if (classId == null)
                continue;

            classDay = classDayMap.get(classId);

            if (classDay == null)
                continue;

            if (!Util.isSameDay(classTrack.getDate(), classDay.getDate())) {

                classTrack.setDate(classDay.getDate());

                fireBaseRepo.updateClassTrackDate(memberId, classId, classTrack.getDate());

            }

        }



    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    @NonNull
    public LiveData<Response> getResponse() {
        return response;
    }

    public void setCurrentMember(String memberId) {
        currentMember.setValue(memberId);
    }

    public void setCurrentClass(String classId) {
        ClassTrack classTrack = new ClassTrack();
        classTrack.setKey(classId);
        getCurrentClass().setValue(classTrack);
    }

    @NonNull
    private LiveData<DataResult<MemberTrack>> getMemberTrackLive() {
        if (memberTrack == null)
            memberTrack = Transformations.switchMap(
                    currentMember, new Function<String, LiveData<DataResult<MemberTrack>>>() {
                        @Override
                        public LiveData<DataResult<MemberTrack>> apply(@Nullable String memberId) {
                            if (memberId == null)
                                return new AbsentLiveData<>();
                            return fireBaseRepo.getMemberTrack(memberId);
                        }
                    }
            );
        return memberTrack;
    }

    @NonNull
    public LiveData<Profile> getProfile() {
        if (profile == null) {
            profile = new MediatorLiveData<>();
            profile.addSource(
                    getMemberTrackLive(), new Observer<DataResult<MemberTrack>>() {
                        @Override
                        public void onChanged(@Nullable DataResult<MemberTrack> dataResult) {

                            if (dataResult != null && dataResult.getDatabaseError() != null) {
                                response.setValue(
                                        new Response(
                                                false,
                                                dataResult.getDatabaseError().getMessage()
                                        )
                                );
                                return;
                            }

                            if (dataResult != null && dataResult.getValue() != null)
                                profile.setValue(dataResult.getValue().getProfile());

                        }
                    }
            );
        }
        return profile;
    }

    @NonNull
    private LiveData<List<ClassTrack>> getClassTrackList() {

        if (classTrackList == null) {

            classTrackList = new MediatorLiveData<>();

            classTrackList.addSource(
                    getMemberTrackLive(), new Observer<DataResult<MemberTrack>>() {
                        @Override
                        public void onChanged(@Nullable DataResult<MemberTrack> dataResult) {

                            if (dataResult != null && dataResult.getDatabaseError() != null)

                                response.setValue(
                                        new Response(
                                                false,
                                                dataResult.getDatabaseError().getMessage()
                                        )
                                );

                            else if (
                                    dataResult == null ||
                                            dataResult.getValue() == null ||
                                            dataResult.getValue().getClasses() == null
                            )

                                classTrackList.setValue(null);

                            else {

                                Map<String, ClassTrack> trackMap = dataResult.getValue().getClasses();

                                ClassTrack classTrack;
                                for (String classId : trackMap.keySet()) {
                                    if (classId == null)
                                        continue;
                                    if ((classTrack = trackMap.get(classId)) == null)
                                        continue;
                                    classTrack.setKey(classId);
                                }

                                List<ClassTrack> classTracks = new ArrayList<>(trackMap.values());

                                Sort.byTrack(classTracks);

                                mixDates();

                                classTrackList.setValue(classTracks);

                            }
                        }
                    }
            );

            addSource(classTrackList);

        }

        return classTrackList;

    }

    @NonNull
    public MutableLiveData<ClassTrack> getCurrentClass() {
        if (classTrackLive == null) {
            classTrackLive = new MediatorLiveData<>();
            classTrackLive.addSource(
                    getClassTrackList(), new Observer<List<ClassTrack>>() {
                        @Override
                        public void onChanged(@Nullable List<ClassTrack> classTrackList) {

                            // Si no hay lista de seguimiento dejamos la clase actual
                            if (classTrackList == null || classTrackList.size() == 0)
                                return;

                            // Obtenemos la clase actual
                            ClassTrack currentClass = classTrackLive.getValue();

                            // Si no hay clase actual asignamos la última de la lista
                            if (currentClass == null || currentClass.getKey() == null)
                                classTrackLive.setValue(
                                        classTrackList.get(classTrackList.size() - 1)
                                );

                            // Si existe clase actual la buscamos en la lista recivida para
                                // recargarla con los valores actualizados
                            else
                                for (ClassTrack classTrack : classTrackList)
                                    if (currentClass.getKey().equals(classTrack.getKey())) {
                                        classTrackLive.setValue(classTrack);
                                        return;
                                    }

                        }
                    }
            );
        }
        return classTrackLive;
    }

    @NonNull
    public LiveData<Observation> getObservation() {
        if (observation == null) {
            observation = new MediatorLiveData<>();
            observation.addSource(
                    getCurrentClass(), new Observer<ClassTrack>() {
                        @Override
                        public void onChanged(@Nullable ClassTrack classTrack) {
                            if (classTrack == null)
                                observation.setValue(null);
                            else
                                observation.setValue(classTrack.getObservation(
                                        fireBaseRepo.getUid()
                                ));
                        }
                    }
            );
        }
        return observation;
    }

    public MutableLiveData<Float> getAverage(){
        if (average == null) {
            average = new MediatorLiveData<>();
            average.addSource(
                    getClassTrackList(), new Observer<List<ClassTrack>>() {
                        @Override
                        public void onChanged(@Nullable List<ClassTrack> classTrackList) {
                            if (classTrackList == null || classTrackList.size() == 0)
                                average.setValue(null);
                            else
                                average.setValue(getAverageRate(
                                        classTrackList, fireBaseRepo.getUid()
                                ));
                        }
                    }
            );
        }
        return average;
    }

    public void saveTrack(Observation observation) {

        String memberId = currentMember.getValue();

        if (memberId == null)
            return;

        ClassTrack classTrack = getCurrentClass().getValue();

        String classId;
        if (classTrack == null || (classId = classTrack.getKey()) == null)
            return;

        fireBaseRepo.updateTrack(memberId, classId, observation).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.setValue(new Response(false, e.getMessage()));
                    }
                }
        );

    }

    public void updateProfile(Profile profile) {

        MemberTrack memberTrack = getMemberTrack();

        if (memberTrack == null)
            return;

        String memberId = memberTrack.getKey();
        String courseId = memberTrack.getCourse_id();

        if (memberId == null || courseId == null)
            return;

        fireBaseRepo.updateMemberProfile(memberId, courseId, profile)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        response.setValue(new Response(false, e.getMessage()));
                    }
                });

    }

    private MemberTrack getMemberTrack() {

        DataResult<MemberTrack> memberTrackDataResult = getMemberTrackLive().getValue();

        if (memberTrackDataResult == null)
            return null;

        return memberTrackDataResult.getValue();
    }

    @Nullable
    private Float getAverageRate(@NonNull List<ClassTrack> classTrackList, @Nullable String observerUid) {

        int count = 0;
        Float totalRate = 0F;

        Map<String, Observation> observationMap;
        for (ClassTrack classTrack : classTrackList) {
            observationMap = classTrack.getObservations();
            if (observationMap == null)
                continue;
            for (String observer : observationMap.keySet()){
                Observation observation = observationMap.get(observer);
                if (
                        observation != null &&
                                observation.getRate() != null &&
                                observer.equals(observerUid)
                ) {
                    count++;
                    totalRate += observation.getRate();
                }
            }
        }

        return (count == 0) ? null : totalRate / (float) count;
    }


    public void setPreviousClass() {

        List<ClassTrack> classTrackList = getClassTrackList().getValue();
        if (classTrackList == null || classTrackList.size() == 0)
            return;

        ClassTrack currentClass = getCurrentClass().getValue();
        if (currentClass == null)

            getCurrentClass().setValue(classTrackList.get(classTrackList.size() - 1));

        else if (classTrackList.indexOf(currentClass) > 0)

            getCurrentClass().setValue(classTrackList.get(classTrackList.indexOf(currentClass) - 1));

    }

    public void setNextClass() {

        List<ClassTrack> classTrackList = getClassTrackList().getValue();
        if (classTrackList == null || classTrackList.size() == 0)
            return;

        ClassTrack currentClass = getCurrentClass().getValue();
        if (currentClass == null)

            getCurrentClass().setValue(classTrackList.get(classTrackList.size() - 1));

        else if (classTrackList.indexOf(currentClass) < classTrackList.size() - 1)

            getCurrentClass().setValue(classTrackList.get(classTrackList.indexOf(currentClass) + 1));

    }

}
