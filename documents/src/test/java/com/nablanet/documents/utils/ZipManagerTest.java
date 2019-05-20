package com.nablanet.documents.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ZipManagerTest {

    @Test
    public void unzip() {

        File file = FileManager.getFile("tracking.ods");

        try {
            ZipManager.unzip(file, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void zipODF() {

        File fileSrc = FileManager.getFile("tracking");

        assertNotNull(fileSrc);

        try {
            ZipManager.zipODF(fileSrc, new File(fileSrc.getParentFile(), "tracking_modified.ods"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void newFile() {
    }
}