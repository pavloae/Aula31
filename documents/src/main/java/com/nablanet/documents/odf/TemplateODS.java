package com.nablanet.documents.odf;

import com.nablanet.documents.NameSpaceResolver;
import com.nablanet.documents.Parser;
import com.nablanet.documents.odf.content.Body;
import com.nablanet.documents.utils.FileManager;
import com.nablanet.documents.utils.ZipManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class TemplateODS {

    public static final String MODEL = "tracking.ods";

    File template, extractFolder, contentFile, metaFile;
    Document contentDoc, metaDoc;
    public Body body;

    public TemplateODS() {
        this.template = FileManager.getFile(MODEL);
        this.body = getBody();
    }

    public File saveTo(String name) {
        File target = new File(template.getParent(), name);
        try {
            Parser.saveDoc(getContentDoc(), contentFile);
            Parser.saveDoc(getMetaDoc(), metaFile);
            ZipManager.zipODF(extractFolder, target);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return target;
    }

    private Body getBody() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(new NameSpaceResolver(getContentDoc()));
        try {
            Node node = (Node) xPath.evaluate(
                    "/office:document-content/office:body", getContentDoc(), XPathConstants.NODE
            );
            return new Body((Element) node);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Document getContentDoc() {
        if (contentDoc == null)
            contentDoc = Parser.parseXMLFile(getContentFile());
        return contentDoc;
    }

    private Document getMetaDoc() {
        if (metaDoc == null)
            metaDoc = Parser.parseXMLFile(getMetaFile());
        return metaDoc;
    }

    public File getContentFile() {
        if (contentFile == null)
            extractContentFile();
        return contentFile;
    }

    private File getMetaFile() {
        if (metaFile == null)
            extractMetaFile();
        return metaFile;
    }

    private void extractContentFile() {
        try {
            if (extractFolder == null || !extractFolder.exists())
                extractFolder = ZipManager.unzip(template, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (File child : Objects.requireNonNull(extractFolder.listFiles()))
            if (child.getName().equals("content.xml")) {
                contentFile = child;
                return;
            }
    }

    private void extractMetaFile() {
        try {
            if (extractFolder == null || !extractFolder.exists())
                extractFolder = ZipManager.unzip(template, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (File child : Objects.requireNonNull(extractFolder.listFiles()))
            if (child.getName().equals("meta.xml")) {
                metaFile = child;
                return;
            }
    }

}
