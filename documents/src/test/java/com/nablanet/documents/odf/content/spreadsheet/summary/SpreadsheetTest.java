package com.nablanet.documents.odf.content.spreadsheet.summary;

import com.nablanet.documents.odf.TemplateODS;
import com.nablanet.documents.utils.FileManager;

import org.junit.Test;
import org.w3c.dom.Document;

import java.io.File;

public class SpreadsheetTest {

    @Test
    public void createSheets() {

        TemplateODS templateODS = new TemplateODS();

        Document contentDoc = templateODS.getContentDoc();

    }
}