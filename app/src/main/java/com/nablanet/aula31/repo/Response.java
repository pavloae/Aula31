package com.nablanet.aula31.repo;

public class Response {

    boolean success;
    int code;
    String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Response(int code, String message) {
        this.success = true;
        this.code = code;
        this.message = message;
    }

    public Response(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess(){
        return success;
    }

    public int getCode() {
        return code;
    }
}
