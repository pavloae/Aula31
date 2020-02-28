package com.nablanet.aula31;

import androidx.annotation.NonNull;

public interface OnItemListener<T> {

    void onItemClick(@NonNull T object);

    boolean onItemLongClick(@NonNull T object);

}
