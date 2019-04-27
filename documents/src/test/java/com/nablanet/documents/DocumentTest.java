package com.nablanet.documents;

import org.junit.Test;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.transform.TransformerException;

public class DocumentTest {

    @Test
    public void unzip() {

        URL url = ClassLoader.getSystemClassLoader().getResource("tracking_deflated.ods");

        if (url == null) return;

        URI uri = null;

        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (uri == null) return;

        File file = new File(uri);

        try {
            Document.unzip(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void parseXMLFile() {

        URL url = ClassLoader.getSystemClassLoader().getResource("unziped/content.xml");

        if (url == null) return;

        URI uri = null;

        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (uri == null) return;

        File file = new File(uri);

        org.w3c.dom.Document document = Parser.parseXMLFile(file);

        Node body = Parser.getBody(document);

        assert body != null;

        Node spreadsheet = Parser.getSpreedSheet(body);

        assert spreadsheet != null;

        Node tp01 = Parser.getSheet(spreadsheet, "TP01");

        assert tp01 != null;

        Node tp02 = tp01.cloneNode(true);

        tp02.getAttributes().getNamedItem("table:name").setNodeValue("TP02");

        tp01.getParentNode().insertBefore(tp02, tp01.getNextSibling());

        File target = new File(file.getParentFile(), "content2.xml");

        try {
            Parser.saveDoc(document, target);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void deleteFile() {
        URL url = ClassLoader.getSystemClassLoader().getResource("unziped/content.xml");

        if (url == null) return;

        URI uri = null;

        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (uri == null) return;

        File file = new File(uri);

        Document.deleteFolder(file);

    }

    @Test
    public void moveFile() {

        URL urlSrc = ClassLoader.getSystemClassLoader().getResource("unziped/content2.xml");

        if (urlSrc == null) return;

        URI uri = null;

        try {
            uri = urlSrc.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (uri == null) return;

        File fileSrc = new File(uri);
        File fileTar = new File(fileSrc.getParentFile(), "content.xml");

        try {
            Document.copyFile(fileSrc, fileTar);
            Document.deleteFolder(fileSrc);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void zipFile() {

        URL urlSrc = ClassLoader.getSystemClassLoader().getResource("unziped");
        if (urlSrc == null) return;
        URI uri = null;
        try {
            uri = urlSrc.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (uri == null) return;

        File fileSrc = new File(uri);


        try {
            Document.zipDirectory(fileSrc, new File(fileSrc.getParentFile(), "tracking_modified.ods"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}