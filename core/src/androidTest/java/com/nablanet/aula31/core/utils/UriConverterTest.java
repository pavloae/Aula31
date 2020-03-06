package com.nablanet.aula31.core.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class UriConverterTest {

    @Test
    public void getUri() throws URISyntaxException {

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        File file = new File(appContext.getCacheDir(), "testFile.txt");

        Uri uri1 = Uri.fromFile(file);

        URI Uri = UriConverter.getURI(uri1);

        Uri uri2 = UriConverter.getUri(Uri);

        assertEquals(uri1.toString(), uri2.toString());

        assertEquals(uri2.getLastPathSegment(), "testFile.txt");




    }

    @Test
    public void getURI() {

        String separator = File.separator;

        Log.d("TEST","Separartor: " + separator);

    }
}