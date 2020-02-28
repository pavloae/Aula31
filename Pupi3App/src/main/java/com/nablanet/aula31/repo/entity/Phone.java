package com.nablanet.aula31.repo.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/phones/$pid/"
 */
public class Phone extends KeyImpl {

    @Nullable private String uid;
    @Nullable private Boolean share;

    @Nullable
    public String getUid() {
        return uid;
    }

    public void setUid(@Nullable String uid) {
        this.uid = uid;
    }

    @Nullable
    public Boolean getShare() {
        return share;
    }

    public void setShare(@Nullable Boolean share) {
        this.share = share;
    }

    @NonNull
    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        if (uid != null) result.put("uid", uid);
        if (share != null) result.put("share", share);
        return result;
    }

}
