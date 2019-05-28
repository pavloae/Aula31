package com.nablanet.documents.odf.content;

import com.nablanet.documents.odf.content.spreadsheet.Spreadsheet;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

public class Body extends BaseElement{

    private Spreadsheet spreadsheet;

    public Body(Element element) {
        super(element);
    }

    public Spreadsheet getSpreadsheet() throws XPathExpressionException {
        if (spreadsheet != null) return spreadsheet;
        Node node = (Node) getXPath().evaluate(
                "/office:document-content/office:body/office:spreadsheet",
                getDocument(), XPathConstants.NODE
        );
        spreadsheet = new Spreadsheet((Element) node);
        return spreadsheet;
    }

}
