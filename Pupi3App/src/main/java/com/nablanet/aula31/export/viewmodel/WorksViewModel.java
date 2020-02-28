package com.nablanet.aula31.export.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.annotation.Nullable;

import com.nablanet.aula31.ExecutorFactory;
import com.nablanet.aula31.export.data.DataParams;
import com.nablanet.aula31.export.data.DataWorkImpl;
import com.nablanet.aula31.export.factory.DataWorkFactory;
import com.nablanet.aula31.repo.DataResult;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.entity.CourseWork;
import com.nablanet.aula31.repo.entity.MemberRepo;

import java.util.List;
import java.util.concurrent.Executor;

public class WorksViewModel extends ViewModel {

    private Executor executor = ExecutorFactory.getInstanceSingleThreadExecutor();
    private FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    MediatorLiveData<List<DataWorkImpl>> dataWorkListLive;

    LiveData<List<DataWorkImpl>> getDataWorkList(
            final DataParams dataParams, final Observer<Response> responseObserver
    ){

        dataWorkListLive = new MediatorLiveData<>();

        if (dataParams == null || dataParams.getCourseId() == null || dataParams.getMemberIdList() == null){
            dataWorkListLive.setValue(null);
            return dataWorkListLive;
        }

        String courseId = dataParams.getCourseId();

        final DataWorkFactory dataWorkFactory = new DataWorkFactory();
        final LiveData<DataResult<MemberRepo>> membersRepositories = fireBaseRepo
                .getMembersRepositories(courseId);
        final LiveData<DataResult<CourseWork>> courseWorks = fireBaseRepo
                .getCourseWorks(courseId);

        dataWorkListLive.addSource(membersRepositories, new Observer<DataResult<MemberRepo>>() {

            @Override
            public void onChanged(@Nullable final DataResult<MemberRepo> dataResult) {
                dataWorkListLive.removeSource(membersRepositories);

                if (dataResult != null && dataResult.getDatabaseError() != null)

                    responseObserver.onChanged(
                            new Response(false, dataResult.getDatabaseError().getMessage())
                    );

                else

                    executor.execute(
                            new Runnable() {
                                @Override
                                public void run() {

                                    dataWorkFactory.setMemberRepoMap(
                                            (dataResult == null) ? null : dataResult.getMap()
                                    );

                                    if (dataWorkFactory.isComplete())

                                        dataWorkListLive.postValue(
                                                dataWorkFactory.getDataWorkList(dataParams)
                                        );

                                }
                            }
                    );
            }
        });

        dataWorkListLive.addSource(courseWorks, new Observer<DataResult<CourseWork>>() {

            @Override
            public void onChanged(@Nullable final DataResult<CourseWork> dataResult) {
                dataWorkListLive.removeSource(courseWorks);

                if (dataResult != null && dataResult.getDatabaseError() != null)

                    responseObserver.onChanged(
                            new Response(false, dataResult.getDatabaseError().getMessage())
                    );

                else

                    executor.execute(
                            new Runnable() {
                                @Override
                                public void run() {

                                    dataWorkFactory.setCourseWorkMap(
                                            (dataResult == null) ? null : dataResult.getMap()
                                    );

                                    if (dataWorkFactory.isComplete())

                                        dataWorkListLive.postValue(
                                                dataWorkFactory.getDataWorkList(dataParams)
                                        );
                                }
                            }
                    );

            }
        });

        return dataWorkListLive;

    }

}
