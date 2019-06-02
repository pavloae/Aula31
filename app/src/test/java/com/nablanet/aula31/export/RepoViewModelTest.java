package com.nablanet.aula31.export;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.nablanet.aula31.export.data.DataParams;
import com.nablanet.aula31.export.data.DataTrackImpl;
import com.nablanet.aula31.export.data.DataWorkImpl;
import com.nablanet.aula31.export.entity.CourseExt;
import com.nablanet.aula31.export.viewmodel.RepoViewModel;
import com.nablanet.aula31.repo.DataResult;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.repo.entity.ClassTrack;
import com.nablanet.aula31.repo.entity.MemberTrack;
import com.nablanet.aula31.repo.entity.Observation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RepoViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();

    private RepoViewModel viewModel;

    @Mock
    public FireBaseRepo mockedFirebaseRepo;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new RepoViewModel();
        //viewModel.fireBaseRepo = mockedFirebaseRepo;
        viewModel.executor = new MainExecutor();

        when(mockedFirebaseRepo.getUid()).thenReturn("ownUid");

    }

    @Test
    public void getDataSummary() {

        RepoViewModel viewModel = mock(RepoViewModel.class);
        viewModel.executor = new MainExecutor();

        DataParams dataParams = null;//new DataParams("course1", null, null, null);

        MutableLiveData<DataTrackImpl> dataTrackLiveData = new MutableLiveData<>();
        MutableLiveData<List<DataWorkImpl>> dataWorkListLiveData = new MutableLiveData<>();
        MutableLiveData<DataResult<ClassDay>> dataResultLiveData = new MutableLiveData<>();
        MutableLiveData<CourseExt> courseExportMutableLiveData = new MutableLiveData<>();

        //when(viewModel.getFireBaseRepo()).thenReturn(mockedFirebaseRepo);
        when(viewModel.getResponseLive()).thenCallRealMethod();
        //when(viewModel.getCourseExportLiveData(dataParams.getCourseId())).thenReturn(courseExportMutableLiveData);
        when(mockedFirebaseRepo.getCourseClasses(dataParams.getCourseId())).thenReturn(dataResultLiveData);

        viewModel.getResponseLive().observeForever(
                new Observer<Response>() {
                    @Override
                    public void onChanged(@Nullable Response response) {
                        if (response != null)
                            System.out.println(response.getMessage());
                    }
                }
        );

        DatabaseError databaseError = mock(DatabaseError.class);
        when(databaseError.getMessage()).thenReturn("mockError");

        DataResult<ClassDay> dayDataResult = mock(DataResult.class);
        when(dayDataResult.getDatabaseError()).thenReturn(databaseError);

        dataTrackLiveData.postValue(null);
        dataWorkListLiveData.postValue(null);
        dataResultLiveData.postValue(new DataResult<ClassDay>(databaseError));

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
    @SuppressWarnings("unchecked")
    public void getDataTrack() {

        Map<String, MemberTrack> memberTrackMap = new HashMap<>();

        ClassTrack classTrack = new ClassTrack();
        classTrack.observations = new HashMap<>();
        classTrack.observations.put("ownUid", new Observation());

        MemberTrack memberTrack1 = new MemberTrack();
        memberTrack1.classes = new HashMap<>();
        memberTrack1.classes.put("class1", classTrack);

        memberTrackMap.put("member1", memberTrack1);

        DataResult dataResult = mock(DataResult.class);
        when(dataResult.getMap()).thenReturn(memberTrackMap);

        MutableLiveData<DataResult<MemberTrack>> liveData = new MutableLiveData<>();
        when(mockedFirebaseRepo.getTrackByCourse("curso1")).thenReturn(liveData);


        List<String> memberList = new ArrayList<>();
        memberList.add("member1");
        memberList.add("member2");
        memberList.add("member3");

        DataParams dataParams = null;//new DataParams("curso1", memberList, null, null);


        liveData.postValue(dataResult);

    }

    @Test
    public void builDataTrack() {

        // Si no hay miembros en el curso y no hay una lista de miembros
        Map<String, MemberTrack> emptyMemberTrackMap = new HashMap<>();
        DataParams dataParamsNonList = null;//new DataParams("course1", null, null, null);
        //Assert.assertNull(viewModel.postDataTrack(emptyMemberTrackMap, dataParamsNonList));

        // Si no hay miembros en el curso y no hay miembros en la lista de selección
        List<String> emptyMemberIdList = new ArrayList<>();
        DataParams dataParamsEmptyList = null;//new DataParams("course1", emptyMemberIdList, null, null);
        //Assert.assertNull(viewModel.postDataTrack(emptyMemberTrackMap, dataParamsEmptyList));

        // Si hay miembros en el curso pero no hay miembros en la lista de selección
        Map<String, MemberTrack> memberTrackMap = new HashMap<>();
        memberTrackMap.put("member1", new MemberTrack());
        memberTrackMap.put("member2", new MemberTrack());
        memberTrackMap.put("member3", new MemberTrack());
        memberTrackMap.put("member4", new MemberTrack());
        //Assert.assertNull(viewModel.postDataTrack(memberTrackMap, dataParamsEmptyList));

        // Si no hay miembros en el curso pero hay una lista de selección
        List<String> memberIdList = new ArrayList<>();
        memberIdList.add("member5");
        memberIdList.add("member6");
        memberIdList.add("member7");
        memberIdList.add("member9");
        DataParams dataParams = null;//new DataParams("course1", memberIdList, null, null);
        //Assert.assertNull(viewModel.postDataTrack(emptyMemberTrackMap, dataParams));

        // Si hay miembros en el curso y hay una lista de selección pero no coincide ninguno
        //Assert.assertNull(viewModel.postDataTrack(memberTrackMap, dataParams));

        // Si hay miembros en el curso y hay una lista de selección coincidiendo dos miembros pero
        // sin clases con observaciones
        memberTrackMap.put("member5", new MemberTrack());
        memberTrackMap.put("member6", new MemberTrack());
        memberTrackMap.put("member8", new MemberTrack());
        //Assert.assertNull(viewModel.postDataTrack(memberTrackMap, dataParams));

        // Si el mimebro Nº5 tiene un mapa de observaciones pero vacio
        MemberTrack member5 = memberTrackMap.get("member5");
        assert member5 != null;
        member5.classes = new HashMap<>();
        //Assert.assertNull(viewModel.postDataTrack(memberTrackMap, dataParams));

        // Si al miembro Nº5 le agregamos una clase de observación vacía
        ClassTrack classTrack1 = new ClassTrack();
        member5.classes.put("clase1", classTrack1);
        //Assert.assertNull(viewModel.postDataTrack(memberTrackMap, dataParams));

        // Si al miembro Nº5 le agregamos una clase de observación con un mapa de observaciones vacío
        ClassTrack classTrack2 = new ClassTrack();
        classTrack2.observations = new HashMap<>();
        member5.classes.put("clase2", classTrack2);
        //Assert.assertNull(viewModel.postDataTrack(memberTrackMap, dataParams));

        // Si al miembro Nº5 le agregamos una observación de otros usuarios
        classTrack2.observations.put("user1uid", new Observation());
        classTrack2.observations.put("user2uid", new Observation());
        //Assert.assertNull(viewModel.postDataTrack(memberTrackMap, dataParams));

        // Si al miembro Nº5 le agregamos una observación nuestra sin fecha y no ponemos
        // límites de tiempo a la consulta
        classTrack2.observations.put("ownUid", new Observation());
        //Assert.assertNotNull(viewModel.postDataTrack(memberTrackMap, dataParams));

        // Si agregamos un límite de tiempo
        dataParams.setFrom(1000L);
        //Assert.assertNull(viewModel.postDataTrack(memberTrackMap, dataParams));

        // Si ponemos el día de la clase 2
        classTrack2.date = 8000L;
        //Assert.assertNotNull(viewModel.postDataTrack(memberTrackMap, dataParams));

        // Si ponemos un límite superior por debajo
        dataParams.setTo(6000L);
        //Assert.assertNull(viewModel.postDataTrack(memberTrackMap, dataParams));

        // Si ponemos un límite superior por arriba
        dataParams.setTo(9000L);
        //Assert.assertNotNull(viewModel.postDataTrack(memberTrackMap, dataParams));

        // Si eliminamos el límite inferior
        dataParams.setFrom(null);
        //DataTrackImpl dataTrack = viewModel.postDataTrack(memberTrackMap, dataParams);
        //Assert.assertNotNull(dataTrack);

        /*List<DataTrackImpl.MemberImpl> memberList = dataTrack.getMembers();
        Assert.assertNotNull(memberList);
        Assert.assertEquals(1, memberList.size());

        DataTrackImpl.MemberImpl member = memberList.get(0);
        Assert.assertNotNull(member);
        Assert.assertEquals("member5", member.getMemberId());
        Assert.assertNotNull(member.getListDay());
        Assert.assertEquals(1, member.getListDay().size());*/

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