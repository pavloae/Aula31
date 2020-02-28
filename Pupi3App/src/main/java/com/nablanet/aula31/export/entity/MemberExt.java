package com.nablanet.aula31.export.entity;

import com.google.firebase.database.Exclude;
import com.nablanet.aula31.repo.entity.Member;

import java.util.Locale;

public class MemberExt extends Member {

    @Exclude
    public boolean checked;

    public MemberExt(Member member) {
        setKey(member.getKey());
        setUser_id(member.getUser_id());
        setProfile(member.getProfile());
        setRole(member.getRole());
        setState(member.getState());
    }

    public String getFullName() {
        return String.format(Locale.getDefault(), "%s, %s", getLastname(), getNames());
    }
}
