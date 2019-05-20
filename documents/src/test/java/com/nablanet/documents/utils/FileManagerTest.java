package com.nablanet.documents.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class FileManagerTest {

    @Test
    public void getFile() {

        File file1 = FileManager.getFile("tracking.ods");
        File file2 = FileManager.getFile("tracking_deflated.ods");
        File file3 = FileManager.getFile("tracking_deflated.xls");

        assertNotNull(file1);
        assertNotNull(file2);
        assertNull(file3);

    }

    @Test
    public void delete() {
    }

    @Test
    public void copy() {
    }

    @Test
    public void move() {

    }

    @Test
    public void createFile() {
    }

    @Test
    public void getExtension() {

        File file = new File("tracking.ods");
        assertEquals("ods", FileManager.getExtension(file));

        file = new File("tracking.ods.zip");
        assertEquals("zip", FileManager.getExtension(file));

        file = new File("tracking");
        assertNull(FileManager.getExtension(file));

    }

    @Test
    public void getPrefix() {

        File file = new File("prueba.ods");
        assertEquals("prueba", FileManager.getPrefix(file));

        file = new File("prueba.ods.zip");
        assertEquals("prueba.ods", FileManager.getPrefix(file));

        file = new File("prueba");
        assertEquals("prueba", FileManager.getPrefix(file));

    }
}