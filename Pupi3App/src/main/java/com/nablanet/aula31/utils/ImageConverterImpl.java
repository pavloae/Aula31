package com.nablanet.aula31.utils;

import android.graphics.Bitmap;
import android.net.Uri;

import com.nablanet.aula31.core.ImageConverter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class ImageConverterImpl implements ImageConverter {

    @Override
    public Bitmap getBitmap(String path) {
        return null;
    }

    @Override
    public Bitmap getBitmap(Uri uri) {
        try {
            URI javaURI = new URI(URLEncoder.encode(uri.toString(), "UTF-8"));
            return getBitmap(javaURI);
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Bitmap getBitmap(URI uri) {
        return null;
    }

    @Override
    public Bitmap getBitmap(File file) {
        return null;
    }

    @Override
    public File getFile(Uri uri) {
        URI javaUri = getURI(uri);
        return (javaUri == null) ? null : new File(javaUri);
    }

    @Override
    public File getFile(URI uri) {
        return new File(uri);
    }

    @Override
    public File getFile(Bitmap bitmap) {
        return null;
    }

    private Uri getUri(URI uri) {
        return null;
    }

    private URI getURI(Uri uri) {
        try {
            return new URI(URLEncoder.encode(uri.toString(), "UTF-8"));
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
