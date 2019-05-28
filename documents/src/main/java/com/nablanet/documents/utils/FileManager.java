package com.nablanet.documents.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

public class FileManager {

    public static final String LAST_POINT = "\\.(?=[^.]+$)";

    public static File getFile(String resource) {

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource(resource);

        if (url == null)
            return null;

        URI uri = null;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return (uri == null) ? null : new File(uri);

    }

    public static boolean delete(File file, boolean recursive) {
        if (file == null) return false;
        if (file.listFiles() != null && recursive)
            for (File child : Objects.requireNonNull(file.listFiles()))
                delete(child, true);
        return file.delete();
    }

    public static void copy(File src, File dst) throws IOException {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(src));
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(dst));
            int b;
            while ((b = bufferedInputStream.read()) != -1)
                bufferedOutputStream.write(b);
        } finally {
            if (bufferedOutputStream != null)
                bufferedOutputStream.close();
            if (bufferedInputStream != null)
                bufferedInputStream.close();
        }
    }

    public static void move(File src, File dst) throws IOException {
        if (dst.exists()) throw new IOException("El destino ya existe");
        if (src.isDirectory()) {
            if (!dst.mkdirs()) throw new IOException("No se pudo crear el directorio");
            for (File child : Objects.requireNonNull(src.listFiles()))
                move(child, new File(dst, child.getName()));
        } else {
            copy(src, new File(dst, src.getName()));
        }
        delete(src, true);
    }

    public static void createFile(InputStream inputStream, File file) throws IOException {

        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            int b;
            while ((b = bufferedInputStream.read()) != -1)
                bufferedOutputStream.write(b);
        } finally {
            if (bufferedOutputStream != null)
                bufferedOutputStream.close();
            if (bufferedInputStream != null)
                bufferedInputStream.close();
        }

    }

    public static String getExtension(File file) {
        String[] name = file.getName().split(LAST_POINT);
        return (name.length == 2) ? name[1] : null;
    }

    public static String getPrefix(File file) {
        return file.getName().split(LAST_POINT)[0];
    }

}
