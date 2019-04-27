package com.nablanet.documents.zip;

import java.io.IOException;

public class ZipManagerTest {

    @org.junit.Test
    public void unzip() {
    }

    @org.junit.Test
    public void unzip1() {


        ZipManager zipManager = new ZipManager();

        try {
            ZipManager.unzip("", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}