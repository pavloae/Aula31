package com.nablanet.aula31.export.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nablanet.aula31.ExecutorFactory;
import com.nablanet.aula31.export.data.DataParams;
import com.nablanet.aula31.export.data.DataTrackImpl;
import com.nablanet.aula31.repo.DataResult;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.entity.ClassTrack;
import com.nablanet.aula31.repo.entity.MemberTrack;
import com.nablanet.aula31.repo.entity.Observation;
import com.nablanet.aula31.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class TrackViewModel extends ViewModel {

    public Executor executor = ExecutorFactory.getInstanceSingleThreadExecutor();
    FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    private MediatorLiveData<DataTrackImpl> dataTrackLive;

    @NonNull
    LiveData<DataTrackImpl> getDataTrack(
            @Nullable final DataParams dataParams, final Observer<Response> responseObserver
    ){

        dataTrackLive = new MediatorLiveData<>();

        if (dataParams == null || dataParams.getCourseId() == null || dataParams.getMemberIdList() == null){
            dataTrackLive.setValue(null);
            return dataTrackLive;
        }

        final LiveData<DataResult<MemberTrack>> trackByCourse = fireBaseRepo
                .getTrackByCourse(dataParams.getCourseId());

        dataTrackLive.addSource(trackByCourse, new Observer<DataResult<MemberTrack>>() {

            @Override
            public void onChanged(@Nullable DataResult<MemberTrack> dataResult) {

                dataTrackLive.removeSource(trackByCourse);

                if (dataResult == null)

                    dataTrackLive.setValue(null);

                else if (dataResult.getDatabaseError() != null)

                    responseObserver.onChanged(new Response(
                                    false, dataResult.getDatabaseError().getMessage()
                            )
                    );

                else

                    postDataTrack(dataResult, dataParams);

            }
        });

        return dataTrackLive;

    }

    private void postDataTrack(
            @NonNull final DataResult<MemberTrack> dataResult, final DataParams dataParams
    ) {

        executor.execute(

                new Runnable() {
                    @Override
                    public void run() {

                        if (dataParams.getMemberIdList() == null) {
                            dataTrackLive.postValue(null);
                            return;
                        }

                        Map<String, MemberTrack> memberTrackMap = dataResult.getMap();

                        if (memberTrackMap == null) {
                            dataTrackLive.postValue(null);
                            return;
                        }

                        // Recorremos cada miembro seleccionado por el usuario
                        List<DataTrackImpl.MemberImpl> memberList = new ArrayList<>();
                        for (String memberId : dataParams.getMemberIdList()) {

                            MemberTrack memberTrack = memberTrackMap.get(memberId);
                            if (memberTrack == null || memberTrack.classes == null)
                                continue;

                            List<DataTrackImpl.DayImpl> dayList = new ArrayList<>();
                            Observation observation;

                            for (ClassTrack classTrack : memberTrack.classes.values()) {
                                if (classTrack == null)
                                    continue;

                                boolean dayCondition = Util.isBetween(
                                        dataParams.getFrom(), classTrack.date, dataParams.getTo()
                                );
                                observation = classTrack.getObservation(fireBaseRepo.getUid());
                                if (dayCondition && observation != null)
                                    dayList.add(new DataTrackImpl.DayImpl(
                                            classTrack.date, observation.rate, observation.comment
                                    ));

                            }

                            if (dayList.size() > 0)
                                memberList.add(new DataTrackImpl.MemberImpl(
                                        memberId, memberTrack.getFullName(), dayList
                                ));

                        }

                        dataTrackLive.postValue(
                                (memberList.size() == 0) ? null : new DataTrackImpl(memberList)
                        );
                    }
                }

        );

    }

}
