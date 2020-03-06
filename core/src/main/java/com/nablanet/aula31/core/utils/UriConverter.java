package com.nablanet.aula31.core.utils;

import android.net.Uri;

import androidx.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UriConverter {

    public static Uri getUri(URI uri) {
        return Uri.parse(uri.toString());
    }

    public static URI getURI(Uri uri) throws URISyntaxException {
        return new URI(uri.toString());
    }

    public static URL getURL(@Nullable Uri uri) throws MalformedURLException {
        if (uri == null)
            throw new NullPointerException();
        return new URL(uri.toString());
    }

}
