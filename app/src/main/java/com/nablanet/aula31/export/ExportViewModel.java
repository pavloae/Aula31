package com.nablanet.aula31.export;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.nablanet.aula31.export.data.DataParams;
import com.nablanet.aula31.export.data.DataRepo;
import com.nablanet.aula31.export.data.DataSummaryImpl;
import com.nablanet.aula31.export.data.DataTrackImpl;
import com.nablanet.aula31.export.data.DataWorkImpl;
import com.nablanet.aula31.export.entity.ClassDayExport;
import com.nablanet.aula31.export.entity.CourseExport;
import com.nablanet.aula31.export.factory.DataRepoFactory;
import com.nablanet.aula31.export.factory.DataSummaryFactory;
import com.nablanet.aula31.export.factory.DataWorkFactory;
import com.nablanet.aula31.repo.DataResult;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.repo.entity.ClassTrack;
import com.nablanet.aula31.repo.entity.Course;
import com.nablanet.aula31.repo.entity.MemberRepo;
import com.nablanet.aula31.repo.entity.MemberTrack;
import com.nablanet.aula31.repo.entity.Observation;
import com.nablanet.aula31.repo.entity.CourseWork;
import com.nablanet.aula31.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExportViewModel extends ViewModel {

    FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();
    Executor executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<Response> responseLive = new MutableLiveData<>();
    private LiveData<CourseExport> courseLive;

    private MediatorLiveData<DataRepo> dataRepoLive;
    private MediatorLiveData<DataSummaryImpl> dataSummaryLive;
    private MediatorLiveData<DataTrackImpl> dataTrackLive;
    private MediatorLiveData<List<DataWorkImpl>> dataWorkListLive;

    @NonNull
    LiveData<Response> getResponseLive() {
        return responseLive;
    }

    @NonNull
    LiveData<CourseExport> getCourseExportLiveData(String courseId) {
        if (courseLive == null)
            courseLive = Transformations.map(
                    fireBaseRepo.getCourse(courseId),
                    new Function<Course, CourseExport>() {
                        @Override
                        public CourseExport apply(Course course) {
                            if (course == null) return null;
                            return new CourseExport(
                                    course.course_id, course.profile, course.members
                            );
                        }
                    }
            );
        return courseLive;
    }

    @NonNull
    public MediatorLiveData<DataRepo> getDataRepoLive(@Nullable final DataParams dataParams) {

        if (dataRepoLive == null) {

            final DataRepoFactory dataRepoFactory = new DataRepoFactory();
            dataRepoLive = new MediatorLiveData<>();

            // DATA TRACK

            dataRepoLive.addSource(
                    getDataTrack(dataParams),
                    new Observer<DataTrackImpl>() {
                        @Override
                        public void onChanged(@Nullable DataTrackImpl dataTrack) {
                            dataRepoLive.removeSource(dataTrackLive);
                            dataRepoFactory.setDataTrack(dataTrack);
                            if (dataRepoFactory.isCompleted())
                                dataRepoLive.setValue(dataRepoFactory.getDataRepo());
                        }
                    }
            );

            // DATA SUMMARY

            dataRepoLive.addSource(
                    getDataSummary(dataParams),
                    new Observer<DataSummaryImpl>() {
                        @Override
                        public void onChanged(@Nullable DataSummaryImpl dataSummary) {
                            dataRepoLive.removeSource(dataSummaryLive);
                            dataRepoFactory.setDataSummary(dataSummary);
                            if (dataRepoFactory.isCompleted())
                                dataRepoLive.setValue(dataRepoFactory.getDataRepo());
                        }
                    }
            );

            // DATA WORK

            dataRepoLive.addSource(
                    getDataWorkList(dataParams), new Observer<List<DataWorkImpl>>() {
                        @Override
                        public void onChanged(@Nullable List<DataWorkImpl> dataWorkList) {
                            dataRepoLive.removeSource(dataWorkListLive);
                            dataRepoFactory.setDataWorkList(dataWorkList);
                            if (dataRepoFactory.isCompleted())
                                dataRepoLive.setValue(dataRepoFactory.getDataRepo());
                        }
                    }
            );

        }

        return dataRepoLive;

    }

    // DATA_TRACK - Datos para la planilla de seguimiento

    @NonNull
    private LiveData<DataTrackImpl> getDataTrack(final DataParams dataParams){
        if (dataTrackLive == null) {
            dataTrackLive = new MediatorLiveData<>();
            final LiveData<DataResult> firebaseRepo = fireBaseRepo
                    .getTrackByCourse(dataParams.getCourseId());
            dataTrackLive.addSource(
                    firebaseRepo,
                    new Observer<DataResult>() {
                        @Override
                        public void onChanged(@Nullable final DataResult dataResult) {
                            dataTrackLive.removeSource(firebaseRepo);

                            if (dataResult == null)
                                dataTrackLive.setValue(null);

                            else if (dataResult.getDatabaseError() != null)
                                responseLive.setValue(
                                        new Response(
                                                false,
                                                dataResult.getDatabaseError().getMessage()
                                        )
                                );

                            else
                                executor.execute(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                buildDataTrack(
                                                        dataResult.getDataSnapshot(), dataParams
                                                );
                                            }
                                        }
                                );
                        }
                    });


        }
        return dataTrackLive;
    }

    @WorkerThread
    private void buildDataTrack(
            @NonNull final DataSnapshot dataSnapshot, @Nullable final DataParams dataParams
    ) {

        Map<String, MemberTrack> memberTrackMap;
        if (
                dataParams == null ||
                        dataParams.getMemberIdList() == null ||
                        (memberTrackMap = dataSnapshot.getValue(
                                new GenericTypeIndicator<Map<String, MemberTrack>>() {})
                        ) == null
        ) {
            dataTrackLive.postValue(null);
            return;
        }

        // Recorremos cada miembro seleccionado por el usuario
        List<DataTrackImpl.MemberImpl> memberList = new ArrayList<>();
        for (String memberId : dataParams.getMemberIdList()) {
            MemberTrack memberTrack = memberTrackMap.get(memberId);
            if (memberTrack == null || memberTrack.classes == null) continue;

            List<DataTrackImpl.DayImpl> dayList = new ArrayList<>();
            Observation observation;
            for (ClassTrack classTrack : memberTrack.classes.values()) {
                if (classTrack == null) continue;
                boolean dayCondition = Util.isBetween(
                        dataParams.getFrom(), dataParams.getTo(), classTrack.date
                );
                observation = classTrack.getObservation(getUid());
                if (dayCondition && observation != null)
                    dayList.add(new DataTrackImpl.DayImpl(
                            classTrack.date, observation.rate, observation.comment
                    ));
            }

            memberList.add(new DataTrackImpl.MemberImpl(
                    memberId, memberTrack.getFullName(), dayList
            ));
        }

        dataTrackLive.postValue(new DataTrackImpl(memberList));
    }

    // DATA_WORK - Datos para la planilla de trabajos

    @NonNull
    private LiveData<List<DataWorkImpl>> getDataWorkList(final DataParams dataParams){
        if (dataWorkListLive == null) {

            dataWorkListLive = new MediatorLiveData<>();
            final DataWorkFactory dataWorkFactory = new DataWorkFactory();
            final LiveData<DataResult> membersRepositories = fireBaseRepo
                    .getMembersRepositories(dataParams.getCourseId());
            final LiveData<DataResult> courseWorks = fireBaseRepo
                    .getCourseWorks(dataParams.getCourseId());

            dataWorkListLive.addSource(
                    membersRepositories, new Observer<DataResult>() {
                        @Override
                        public void onChanged(@Nullable final DataResult dataResult) {
                            dataWorkListLive.removeSource(membersRepositories);
                            executor.execute(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            buildRepositories(
                                                    (dataResult == null) ?
                                                            null : dataResult.getDataSnapshot(),
                                                    dataWorkFactory, dataParams
                                            );
                                        }
                                    }
                            );

                        }
                    }
            );

            dataWorkListLive.addSource(
                    courseWorks, new Observer<DataResult>() {
                        @Override
                        public void onChanged(@Nullable final DataResult dataResult) {
                            dataWorkListLive.removeSource(courseWorks);
                            executor.execute(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            buildWorks(
                                                    (dataResult == null) ?
                                                            null : dataResult.getDataSnapshot(),
                                                    dataWorkFactory, dataParams
                                            );
                                        }
                                    }
                            );

                        }
                    }
            );

        }
        return dataWorkListLive;
    }

    @WorkerThread
    private void buildRepositories(
            DataSnapshot dataSnapshot, DataWorkFactory dataWorkFactory, DataParams dataParams
    ) {
        if (dataSnapshot == null)
            dataWorkFactory.setMemberRepoMap(null);
        else {
            dataWorkFactory.setMemberRepoMap(
                    dataSnapshot.getValue(new GenericTypeIndicator<Map<String, MemberRepo>>() {})
            );
        }

        if (dataWorkFactory.isComplete())
            dataWorkListLive.postValue(dataWorkFactory.getDataWorkList(dataParams));
    }

    @WorkerThread
    private void buildWorks(
            DataSnapshot dataSnapshot, DataWorkFactory dataWorkFactory, DataParams dataParams
    ) {
        if (dataSnapshot == null) {
            dataWorkFactory.setCourseWorkMap(null);
        } else {
            dataWorkFactory.setCourseWorkMap(
                    dataSnapshot.getValue(new GenericTypeIndicator<Map<String, CourseWork>>() {})
            );
        }

        if (dataWorkFactory.isComplete())
            dataWorkListLive.postValue(dataWorkFactory.getDataWorkList(dataParams));
    }

    // DATA_SUMMARY - Datos para la planilla resumen

    @NonNull
    private LiveData<DataSummaryImpl> getDataSummary(final DataParams dataParams) {

        if (dataSummaryLive == null) {

            dataSummaryLive = new MediatorLiveData<>();
            final DataSummaryFactory dataSummaryFactory = new DataSummaryFactory(courseLive.getValue());
            final LiveData<DataResult> classDayList = fireBaseRepo
                    .getCourseClasses(dataParams.getCourseId());

            dataSummaryLive.addSource(
                    getDataTrack(dataParams), new Observer<DataTrackImpl>() {
                        @Override
                        public void onChanged(@Nullable DataTrackImpl dataTrack) {
                            dataSummaryLive.removeSource(dataTrackLive);
                            dataSummaryFactory.setDataTrack(dataTrack);
                            if (dataSummaryFactory.isComplete())
                                executor.execute(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                buildDataSummary(dataSummaryFactory, dataParams);
                                            }
                                        }
                                );
                        }
                    }

            );

            dataSummaryLive.addSource(
                    getDataWorkList(dataParams), new Observer<List<DataWorkImpl>>() {
                        @Override
                        public void onChanged(@Nullable List<DataWorkImpl> dataWorkList) {
                            dataSummaryLive.removeSource(dataWorkListLive);
                            dataSummaryFactory.setDataWorkList(dataWorkList);
                            if (dataSummaryFactory.isComplete())
                                executor.execute(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                buildDataSummary(dataSummaryFactory, dataParams);
                                            }
                                        }
                                );
                        }
                    }
            );

            dataSummaryLive.addSource(
                    classDayList, new Observer<DataResult>() {
                        @Override
                        public void onChanged(@Nullable final DataResult dataResult) {
                            dataSummaryLive.removeSource(classDayList);
                            executor.execute(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            builClassDay(
                                                    (dataResult == null) ? null :
                                                            dataResult.getDataSnapshot(),
                                                    dataSummaryFactory, dataParams
                                            );
                                        }
                                    }
                            );

                        }
                    }
            );
        }
        return dataSummaryLive;
    }

    @WorkerThread
    private void builClassDay(
            @Nullable DataSnapshot dataSnapshot,
            DataSummaryFactory dataSummaryFactory,
            DataParams dataParams
    ) {

        Map<String, ClassDayExport> classDayMap = null;
        if (dataSnapshot != null)
            classDayMap = dataSnapshot.getValue(
                    new GenericTypeIndicator<Map<String, ClassDayExport>>(){}
                    );


        if (dataSnapshot == null || classDayMap == null)
            dataSummaryFactory.setClassDayList(null);
        else {
            List<ClassDayExport> classDayList = new ArrayList<>(classDayMap.values());

            // Las clases quedan ordenadas de la más reciente a la más antigua
            Collections.sort(classDayList, new Comparator<ClassDayExport>() {
                @Override
                public int compare(ClassDayExport o1, ClassDayExport o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });
            Collections.reverse(classDayList);

            dataSummaryFactory.setClassDayList(classDayList);

        }

        if (dataSummaryFactory.isComplete())
            dataSummaryLive.postValue(dataSummaryFactory.getDataSummary(dataParams));
    }

    @WorkerThread
    private void buildDataSummary(DataSummaryFactory dataSummaryFactory, DataParams dataParams) {
        dataSummaryLive.postValue(dataSummaryFactory.getDataSummary(dataParams));
    }


    private String getUid() {
        return FirebaseAuth.getInstance().getUid();
    }

}
