package com.nablanet.aula31.repo.entity;

import com.google.firebase.database.Exclude;

public class KeyImpl implements Key {

    @Exclude
    private String key;

    @Exclude
    @Override
    public String getKey() {
        return key;
    }

    @Exclude
    @Override
    public void setKey(String key) {
        this.key = key;
    }
}
