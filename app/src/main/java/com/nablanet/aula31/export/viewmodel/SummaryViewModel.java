package com.nablanet.aula31.export.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.nablanet.aula31.ExecutorFactory;
import com.nablanet.aula31.export.data.DataParams;
import com.nablanet.aula31.export.data.DataSummaryImpl;
import com.nablanet.aula31.export.data.DataTrackImpl;
import com.nablanet.aula31.export.data.DataWorkImpl;
import com.nablanet.aula31.export.factory.DataSummaryFactory;
import com.nablanet.aula31.repo.DataResult;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.utils.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class SummaryViewModel extends ViewModel {

    public Executor executor = ExecutorFactory.getInstanceSingleThreadExecutor();
    FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    private MediatorLiveData<DataSummaryImpl> dataSummaryLive;

    LiveData<DataSummaryImpl> getDataSummary(
            final DataParams dataParams,
            final LiveData<DataTrackImpl> dataTrackLive,
            final LiveData<List<DataWorkImpl>> dataWorkListLive,
            final Observer<Response> responseObserver
    ) {

        dataSummaryLive = new MediatorLiveData<>();

        if (dataParams == null || dataParams.getCourseId() == null || dataParams.getMemberIdList() == null){
            dataSummaryLive.setValue(null);
            return dataSummaryLive;
        }

        final DataSummaryFactory dataSummaryFactory = new DataSummaryFactory();
        dataSummaryFactory.setCourseExport(dataParams.getCourseExport());

        dataSummaryLive.addSource(dataTrackLive, new Observer<DataTrackImpl>() {

            @MainThread
            @Override
            public void onChanged(@Nullable DataTrackImpl dataTrack) {
                dataSummaryLive.removeSource(dataTrackLive);

                dataSummaryFactory.setDataTrack(dataTrack);

                if (dataSummaryFactory.isComplete())

                    postDataSummary(dataParams, dataSummaryFactory);

            }
        });

        dataSummaryLive.addSource(dataWorkListLive, new Observer<List<DataWorkImpl>>() {

            @MainThread
            @Override
            public void onChanged(@Nullable List<DataWorkImpl> dataWorkList) {
                dataSummaryLive.removeSource(dataWorkListLive);

                dataSummaryFactory.setDataWorkList(dataWorkList);

                if (dataSummaryFactory.isComplete())

                    postDataSummary(dataParams, dataSummaryFactory);

            }
        });

        final LiveData<DataResult<ClassDay>> classDayList = fireBaseRepo
                .getCourseClasses(dataParams.getCourseId());

        dataSummaryLive.addSource(classDayList, new Observer<DataResult<ClassDay>>() {

            @MainThread
            @Override
            public void onChanged(@Nullable final DataResult<ClassDay> dataResult) {
                dataSummaryLive.removeSource(classDayList);

                if (dataResult != null && dataResult.getDatabaseError() != null)

                    responseObserver.onChanged(new Response(
                            false, dataResult.getDatabaseError().getMessage()
                    ));

                else

                    process(dataResult, dataParams, dataSummaryFactory);


            }
        });

        return dataSummaryLive;

    }

    private void postDataSummary(
            final DataParams dataParams, final DataSummaryFactory dataSummaryFactory
    ) {

        executor.execute(

                new Runnable() {

                    @Override
                    public void run() {

                        dataSummaryLive.postValue(dataSummaryFactory.getDataSummary(dataParams));

                    }
                }

        );

    }

    private void process(
            final DataResult<ClassDay> dataResult,
            final DataParams dataParams,
            final DataSummaryFactory dataSummaryFactory
    ) {

        executor.execute(
                new Runnable() {
                    @Override
                    public void run() {

                        if (dataResult == null || dataResult.getMap() == null)

                            dataSummaryFactory.setClassDayList(null);

                        else {

                            List<ClassDay> classDayList = new ArrayList<>(
                                    dataResult.getMap().values()
                            );

                            Sort.byDate(classDayList, Sort.ASC);

                            dataSummaryFactory.setClassDayList(classDayList);

                        }

                        if (dataSummaryFactory.isComplete())

                            dataSummaryLive.postValue(dataSummaryFactory.getDataSummary(dataParams));

                    }
                }
        );

    }


}
