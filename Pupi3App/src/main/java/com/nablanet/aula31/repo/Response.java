package com.nablanet.aula31.repo;

public class Response {

    public boolean success;
    String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
