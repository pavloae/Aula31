package com.nablanet.aula31.core.viewmodel;

public class Response<T> {

    public boolean success;
    String message;
    T value;

    public Response(T value, boolean success, String message) {
        this.value = value;
        this.success = success;
        this.message = message;
    }

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public T getValue() {
        return value;
    }

}
