package com.nablanet.documents.odf.content;

import com.nablanet.documents.odf.content.spreadsheet.Spreadsheet;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

public class Body extends BaseElement{

    Spreadsheet spreadsheet;

    public Body(Element element) {
        super(element);
        spreadsheet = getSpreadsheet();
    }

    private Spreadsheet getSpreadsheet() {
        try {
            Node node = (Node) getXPath().evaluate(
                    "/office:document-content/office:body/office:spreadsheet",
                    getDocument(), XPathConstants.NODE
            );
            return new Spreadsheet((Element) node);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
