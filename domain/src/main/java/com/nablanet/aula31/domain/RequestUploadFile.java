package com.nablanet.aula31.domain;

import java.net.URI;

public class RequestUploadFile {

    public String path;
    public URI uri;

    public RequestUploadFile(String path, URI uri) {
        this.path = path;
        this.uri = uri;
    }

}
