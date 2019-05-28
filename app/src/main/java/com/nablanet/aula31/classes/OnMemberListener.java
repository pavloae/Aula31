package com.nablanet.aula31.classes;

import com.nablanet.aula31.OnItemListener;
import com.nablanet.aula31.classes.entity.MemberItem;

public interface OnMemberListener extends OnItemListener<MemberItem> {
    void onMemberTrack(MemberItem memberItem);
}
