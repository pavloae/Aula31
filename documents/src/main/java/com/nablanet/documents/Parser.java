package com.nablanet.documents;

import org.w3c.dom.Document;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Parser {

    public static Document parseXMLFile(File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document document = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(file);
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    public static Element getBody(Document document) {
        Node child = document.getDocumentElement().getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("office:body"))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }

    public static Element getSpreedSheet(Element body) {
        Node child = body.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("office:spreadsheet"))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }

    public static Element getSheet(Element spreadsheet, String sheetName) {
        Node child = spreadsheet.getFirstChild();
        do {
            if (child == null)
                continue;

            if (child.getNodeType() != Node.ELEMENT_NODE)
                continue;

            NamedNodeMap namedNodeMap = child.getAttributes();
            if (namedNodeMap == null)
                continue;

            Node attr = namedNodeMap.getNamedItem("table:name");
            if (attr == null ||
                    attr.getNodeType() != Node.ATTRIBUTE_NODE ||
                    !attr.getNodeValue().equals(sheetName))
                continue;

            return (Element) child;
        } while (child != null && (child = child.getNextSibling()) != null);
        return null;
    }

    public static void saveDoc(Document document, File file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

}
