package com.nablanet.aula31.export.factory;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.nablanet.aula31.export.data.DataParams;
import com.nablanet.aula31.export.data.DataWorkImpl;
import com.nablanet.aula31.repo.entity.MemberWork;
import com.nablanet.aula31.repo.entity.CourseWork;
import com.nablanet.aula31.repo.entity.MemberRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataWorkFactory {

    private Map<String, MemberRepo> memberRepoMap;
    private Map<String, CourseWork> courseWorkMap;

    private boolean memberRepoComplete, courseWorkComplete;

    public void setMemberRepoMap(@Nullable Map<String, MemberRepo> memberRepoMap) {
        this.memberRepoMap = memberRepoMap;
        memberRepoComplete = true;
    }

    public void setCourseWorkMap(@Nullable Map<String, CourseWork> courseWorkMap) {
        this.courseWorkMap = courseWorkMap;
        courseWorkComplete = true;
    }

    @Nullable
    @WorkerThread
    public List<DataWorkImpl> getDataWorkList(@Nullable DataParams dataParams) {

        if (courseWorkMap == null || courseWorkMap.size() == 0 || dataParams == null)
            return null;

        List<DataWorkImpl> dataWorkList = new ArrayList<>();

        // Recorremos cada trabajo del repositorio del curso
        CourseWork courseWork;
        for (String courseWorkId : courseWorkMap.keySet()) {
            if ((courseWork = courseWorkMap.get(courseWorkId))== null)
                continue;

            DataWorkImpl dataWork = new DataWorkImpl();
            dataWork.setRepoWork(courseWork);

            if (dataParams.getMemberIdList() != null) {

                // Para cada miembro seleccionado buscamos la presentación de su trabajo
                List<DataWorkImpl.MemberImpl> memberList = new ArrayList<>();
                for (String memberId : dataParams.getMemberIdList()) {

                    MemberRepo memberRepo = memberRepoMap.get(memberId);
                    if (memberRepo == null || memberRepo.works == null) continue;

                    MemberWork memberWork = memberRepo.works.get(courseWorkId);
                    if (memberWork == null) continue;

                    DataWorkImpl.MemberImpl member = new DataWorkImpl.MemberImpl(
                            memberId, memberRepo.getFullName(), memberWork.date,
                            memberWork.criteria1, memberWork.criteria2, memberWork.criteria3,
                            average(
                                    memberWork.criteria1, courseWork.criteria1.weight,
                                    memberWork.criteria2, courseWork.criteria2.weight,
                                    memberWork.criteria3, courseWork.criteria3.weight
                            ),
                            memberWork.comment
                    );

                    memberList.add(member);
                }
                dataWork.setMembers(memberList);

            }

            dataWorkList.add(dataWork);

        }

        return dataWorkList;
    }

    public boolean isComplete() {
        return memberRepoComplete && courseWorkComplete;
    }

    private Integer average(Integer n1, Integer w1, Integer n2, Integer w2, Integer n3, Integer w3) {

        int sumT = 0;
        Integer sumW = null;

        if (n1 != null && w1 != null) {
            sumT += n1 * w1;
            sumW = w1;
        }

        if (n2 != null && w2 != null) {
            sumT += n2 * w2;
            sumW = (sumW == null) ? w2 : sumW + w2;
        }

        if (n3 != null && w3 != null) {
            sumT += n3 * w3;
            sumW = (sumW == null) ? w3 : sumW + w3;
        }

        if (sumW == null || sumW == 0) return null;

        return (int) ((float) sumT / sumW);

    }

}
