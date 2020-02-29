package com.nablanet.aula31.core.repository.entity;

import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

public class KeyImpl implements Key {

    @Exclude
    @Nullable
    private String key;

    @Nullable
    @Exclude
    @Override
    public String getKey() {
        return key;
    }

    @Exclude
    @Override
    public void setKey(@Nullable String key) {
        this.key = key;
    }
}
