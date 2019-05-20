package com.nablanet.documents;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.transform.TransformerException;

public class DocumentTest {

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

        Element body = Parser.getBody(document);

        assert body != null;

        Element spreadsheet = Parser.getSpreedSheet(body);

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

}