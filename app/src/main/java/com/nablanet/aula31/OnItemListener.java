package com.nablanet.aula31;

public interface OnItemListener<T> {

    void onItemClick(T object);

    boolean onItemLongClick(T object);

}
