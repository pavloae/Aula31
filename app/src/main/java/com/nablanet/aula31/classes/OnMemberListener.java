package com.nablanet.aula31.classes;

import com.nablanet.aula31.OnItemListener;

public interface OnMemberListener extends OnItemListener<ClassDay.Member> {
    void onMemberTrack(ClassDay.Member member);
}
