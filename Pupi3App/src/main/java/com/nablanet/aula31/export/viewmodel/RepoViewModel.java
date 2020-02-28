package com.nablanet.aula31.export.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nablanet.aula31.ExecutorFactory;
import com.nablanet.aula31.export.data.DataParams;
import com.nablanet.aula31.export.data.DataRepo;
import com.nablanet.aula31.export.data.DataSummaryImpl;
import com.nablanet.aula31.export.data.DataTrackImpl;
import com.nablanet.aula31.export.data.DataWorkImpl;
import com.nablanet.aula31.export.factory.DataRepoFactory;
import com.nablanet.aula31.repo.Response;

import java.util.List;
import java.util.concurrent.Executor;

public class RepoViewModel extends ViewModel {

    public Executor executor = ExecutorFactory.getInstanceSingleThreadExecutor();

    private final MutableLiveData<Response> responseLive = new MutableLiveData<>();
    Observer<Response> responseObserver = new Observer<Response>() {
        @Override
        public void onChanged(@Nullable Response response) {
            responseLive.setValue(response);
        }
    };

    private LiveData<DataTrackImpl> dataTrackLive;
    private LiveData<List<DataWorkImpl>> dataWorkListLive;
    private LiveData<DataSummaryImpl> dataSummaryLive;

    private MediatorLiveData<DataRepo> dataRepoLive;
    DataRepoFactory dataRepoFactory;



    @NonNull
    public MediatorLiveData<DataRepo> getDataRepoLive(@Nullable DataParams dataParams) {

        dataTrackLive = new TrackViewModel().getDataTrack(dataParams, responseObserver);
        dataWorkListLive = new WorksViewModel().getDataWorkList(dataParams, responseObserver);
        dataSummaryLive = new SummaryViewModel().getDataSummary(
                dataParams, dataTrackLive, dataWorkListLive, responseObserver
        );

        dataRepoFactory = new DataRepoFactory();

        dataRepoLive = new MediatorLiveData<>();

        // DATA TRACK

        dataRepoLive.addSource(dataTrackLive, new Observer<DataTrackImpl>() {
            @Override
            public void onChanged(@Nullable DataTrackImpl dataTrack) {
                dataRepoLive.removeSource(dataTrackLive);
                dataRepoFactory.setDataTrack(dataTrack);
                if (dataRepoFactory.isCompleted())
                    dataRepoLive.setValue(dataRepoFactory.getDataRepo());
            }
        });

        // DATA SUMMARY

        dataRepoLive.addSource(dataSummaryLive, new Observer<DataSummaryImpl>() {
            @Override
            public void onChanged(@Nullable DataSummaryImpl dataSummary) {
                dataRepoLive.removeSource(dataSummaryLive);
                dataRepoFactory.setDataSummary(dataSummary);
                if (dataRepoFactory.isCompleted())
                    dataRepoLive.setValue(dataRepoFactory.getDataRepo());
            }
        });

        // DATA WORK

        dataRepoLive.addSource(dataWorkListLive, new Observer<List<DataWorkImpl>>() {
            @Override
            public void onChanged(@Nullable List<DataWorkImpl> dataWorkList) {
                dataRepoLive.removeSource(dataWorkListLive);
                dataRepoFactory.setDataWorkList(dataWorkList);
                if (dataRepoFactory.isCompleted())
                    dataRepoLive.setValue(dataRepoFactory.getDataRepo());
            }
        });

        return dataRepoLive;

    }

    @NonNull
    public LiveData<Response> getResponseLive() {
        return responseLive;
    }

}
