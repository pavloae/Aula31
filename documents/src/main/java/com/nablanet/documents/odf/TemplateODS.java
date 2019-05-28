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
import java.io.InputStream;
import java.util.Objects;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class TemplateODS {

    public static final String MODEL = "tracking.ods";

    File template, target, extractFolder, contentFile/*, metaFile*/;
    Document contentDoc/*, metaDoc*/;
    private Body body;


    /**
     * @param target Un archivo en el que se pueda escribir el resultado del documento.
     */
    public TemplateODS(File target) {
        this.target = target;
    }

    public void save() throws TransformerException, IOException {
        Parser.saveDoc(getContentDoc(), contentFile);
        //Parser.saveDoc(getMetaDoc(), metaFile);
        ZipManager.zipODF(extractFolder, target);
    }

    public Body getBody() throws XPathExpressionException {
        if (body != null) return body;
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(new NameSpaceResolver(getContentDoc()));
        Node node = (Node) xPath.evaluate(
                "/office:document-content/office:body", getContentDoc(), XPathConstants.NODE
        );
        body = new Body((Element) node);
        return body;
    }

    private File getTemplate() throws IOException {
        if (template == null)
            createTemplate();
        return template;
    }

    /**
     * Obtenemos un InputStream al recurso con la plantilla y creamos el archivo en el directorio
     * donde se guardará el archivo final
     * @throws IOException Si no se puede crear el archivo
     */
    private void createTemplate() throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(MODEL);
        this.template = new File(target.getParentFile(), MODEL);
        FileManager.createFile(inputStream, this.template);
    }

    public Document getContentDoc() {
        if (contentDoc == null)
            contentDoc = Parser.parseXMLFile(getContentFile());
        return contentDoc;
    }

    public File getContentFile() {
        if (contentFile == null)
            extractContentFile();
        return contentFile;
    }

    private void extractContentFile() {
        try {
            if (extractFolder == null || !extractFolder.exists())
                extractFolder = ZipManager.unzip(getTemplate(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (File child : Objects.requireNonNull(extractFolder.listFiles()))
            if (child.getName().equals("content.xml")) {
                contentFile = child;
                return;
            }
    }

    /*private Document getMetaDoc() {
        if (metaDoc == null)
            metaDoc = Parser.parseXMLFile(getMetaFile());
        return metaDoc;
    }

    private File getMetaFile() {
        if (metaFile == null)
            extractMetaFile();
        return metaFile;
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
    }*/

}
