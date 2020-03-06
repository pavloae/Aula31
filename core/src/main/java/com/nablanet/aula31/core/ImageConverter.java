package com.nablanet.aula31.core;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.net.URI;

public interface ImageConverter {

    Bitmap getBitmap(String path);

    Bitmap getBitmap(Uri uri);

    Bitmap getBitmap(URI uri);

    Bitmap getBitmap(File file);

    File getFile(Uri uri);

    File getFile(URI uri);

    File getFile(Bitmap bitmap);

}
