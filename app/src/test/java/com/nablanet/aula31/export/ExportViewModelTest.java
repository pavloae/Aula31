package com.nablanet.aula31.export;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.nablanet.aula31.export.data.DataParams;
import com.nablanet.aula31.export.data.DataTrackImpl;
import com.nablanet.aula31.repo.DataResult;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.entity.MemberTrack;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ExportViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();

    private ExportViewModel viewModel;

    @Mock
    public FireBaseRepo mockedFirebaseRepo;

    @Mock
    public DataSnapshot dataSnapshot;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new ExportViewModel();
        viewModel.fireBaseRepo = mockedFirebaseRepo;
        viewModel.executor = new MainExecutor();

    }

    public class MainExecutor implements Executor {
        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void onCleared() {
    }

    @Test
    public void getResponseLive() {

    }

    @Test
    public void getCourseExportLiveData() {
    }

    @Test
    public void getDataRepoLive() {
    }

    @Test
    public void getDataTrack() {

        DataSnapshot dataSnapshot = mock(DataSnapshot.class);
        when(dataSnapshot.getValue()).thenReturn(getMemberTrackMap());

        MutableLiveData<DataResult> liveData = new MutableLiveData<>();
        when(mockedFirebaseRepo.getTrackByCourse("curso1")).thenReturn(liveData);

        DataParams dataParams = new DataParams("curso1", getMemberList(), null, null);
        LiveData<DataTrackImpl> dataTrackLiveData = viewModel.getDataTrack(dataParams);
        dataTrackLiveData.observeForever(new Observer<DataTrackImpl>() {
            @Override
            public void onChanged(@Nullable DataTrackImpl dataTrack) {
                Assert.assertNotNull(dataTrack);
            }
        });

        liveData.postValue(new DataResult(dataSnapshot));

    }

    private HashMap<String, MemberTrack> getMemberTrackMap() {
        HashMap<String, MemberTrack> memberTrackHashMap = new HashMap<>();
        MemberTrack memberTrack = new MemberTrack();
        memberTrack.classes = new HashMap<>();
        memberTrackHashMap.put("member1", memberTrack);
        memberTrack = new MemberTrack();
        memberTrack.classes = new HashMap<>();
        memberTrackHashMap.put("member2", memberTrack);

        return memberTrackHashMap;
    }

    private List<String> getMemberList() {
        List<String> memberList = new ArrayList<>();
        memberList.add("member1");
        memberList.add("member3");
        return memberList;
    }

}