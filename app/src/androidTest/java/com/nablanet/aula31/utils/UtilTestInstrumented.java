package com.nablanet.aula31.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.jopendocument.dom.XMLFormatVersion;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class UtilTestInstrumented {

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createPdf() {

        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(100, 100, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawCircle(50, 50, 30, paint);

        // finish the page
        document.finishPage(page);

        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(500, 500, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(200, 200, 100, paint);
        document.finishPage(page);

        // write the document content
        //String targetPdf = "/sdcard/test.pdf";
        String targetPdf = "/data/data/com.nablanet.aula31/databases/test.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close the document
        document.close();
    }

    @Test
    public void createODS() {

        String[] valueset = new String[]{"uno","dos","tres","cuatro","cinco"};

        XMLFormatVersion xmlFormatVersion = XMLFormatVersion.getDefault();

        SpreadSheet spreadSheet = SpreadSheet.create(1,20,50);

        Sheet sheet = spreadSheet.getSheet(0);

        int lineIndex = 1;
        for (String value : valueset) {
            sheet.setValueAt(value,1, lineIndex);

            lineIndex++;
        }

        try {
            spreadSheet.saveAs(new File("/data/data/com.nablanet.aula31/databases/example.ods"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}